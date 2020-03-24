package com.legaoyi.file.server;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.file.message.handler.DeviceDownMessageDeliverer;
import com.legaoyi.file.messagebody.Attachment;
import com.legaoyi.file.server.security.SecurityUtil;
import com.legaoyi.file.server.util.DefaultMessageBuilder;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.ServerRuntimeContext;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class CodecDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CodecDecoder.class);

    private static final ByteBuf delimiter = Unpooled.copiedBuffer(new byte[] {0x30, 0x31, 0x63, 0x64});

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1210->1211->数据文件->1212
        // 1210消息：简单的安全校验simCode；1211：缓存文件名；数据文件：校验文件名；1212：检查是否丢包
        // 8001->8002->.....->9212
        // in.markReaderIndex();
        // byte[] bytes = new byte[in.readableBytes()];
        // logger.error("******接收数据,message={}", ByteUtils.bytes2hex(bytes));
        // in.resetReaderIndex();

        int startIndex = -1;
        while ((startIndex = indexOf(in, delimiter)) != -1) {
            if (startIndex > 0) {
                ByteBuf buf = in.readBytes(startIndex);
                try {
                    // 808协议消息
                    decodeMessage(ctx, buf, out);
                } finally {
                    buf.release();
                }
            }
            try {
                // 去掉开始符号
                in.markReaderIndex();
                in.skipBytes(delimiter.capacity());

                byte[] bytes = new byte[50];
                if (in.readableBytes() > 58) {
                    in.readBytes(bytes);
                } else {
                    in.resetReaderIndex();
                    return;
                }

                String fileName = ByteUtils.bytes2gbk(bytes);
                long offset = in.readUnsignedInt();
                long length = in.readUnsignedInt();
                bytes = new byte[(int) length];
                if (in.readableBytes() >= length) {
                    in.readBytes(bytes);
                } else {
                    in.resetReaderIndex();
                    return;
                }

                Attachment attachment = new Attachment();
                attachment.setFileName(fileName);
                attachment.setOffset(offset);
                attachment.setLength(length);
                attachment.setData(bytes);
                out.add(attachment);
            } catch (Exception e) {
                logger.error("******数据异常", e);
                break;
            }
        }

        if (in.readableBytes() > 0) {
            decodeMessage(ctx, in, out);
        }
    }

    private void decodeMessage(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 2) {
            return;
        }

        while (in.isReadable()) {
            int index = in.indexOf(in.readerIndex() + 1, in.writerIndex(), Message.FLAG);
            if (index != -1) {
                ByteBuf buf = Unpooled.buffer(index + 1 - in.readerIndex());
                try {
                    while (in.readerIndex() <= index) {
                        int b = in.readUnsignedByte();
                        if (b == Message.ESCAPE_CHARACTER_0X7D) {
                            int ext = in.readUnsignedByte();
                            if (ext == Message.ESCAPE_CHARACTER_0X01) {
                                buf.writeByte(Message.ESCAPE_CHARACTER_0X7D);
                            } else if (ext == Message.ESCAPE_CHARACTER_0X02) {
                                buf.writeByte(Message.FLAG);
                            }
                        } else {
                            buf.writeByte(b);
                        }
                    }

                    byte[] bs = new byte[buf.readableBytes()];
                    buf.readBytes(bs);
                    if (bs[0] == Message.FLAG && bs.length > 2) {
                        out.add(bs);
                    } else {
                        in.readerIndex(index);
                        handleIllegalMessage(ctx);
                        logger.error("******数据异常，illegal message, message={}", ByteUtils.bytes2hex(bs));
                    }
                } catch (Exception e) {
                    handleIllegalMessage(ctx);
                    logger.error("", e);
                } finally {
                    buf.release();
                }
            } else {
                break;
            }
        }

        int length = in.readableBytes();
        if (length > 0 && length > 65 * 1024) {
            byte[] bs = new byte[in.readableBytes()];
            in.readBytes(bs);
            handleIllegalMessage(ctx);
            logger.error("******数据异常, message={}", ByteUtils.bytes2hex(bs));
        }
    }

    private void handleIllegalMessage(ChannelHandlerContext ctx) {
        try {
            ServerRuntimeContext.getBean(DeviceDownMessageDeliverer.class).deliver(ctx, DefaultMessageBuilder.build8001Message(2));
        } catch (Exception e) {
            logger.error("******发送应该消息失败", e);
        }
        try {
            // 负载均衡部署时，客户端ip获取不到，获取到的可能是负载均衡服务器的ip，需屏蔽
            String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            // 检查流量控制
            SecurityUtil securityUtil = ServerRuntimeContext.getBean(SecurityUtil.class);
            if (!securityUtil.validateByIp(ip, 1)) {
                ctx.close();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private static int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.capacity(); needleIndex++) {
                if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
                    break;
                } else {
                    haystackIndex++;
                    if (haystackIndex == haystack.writerIndex() && needleIndex != needle.capacity() - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.capacity()) {
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }
}
