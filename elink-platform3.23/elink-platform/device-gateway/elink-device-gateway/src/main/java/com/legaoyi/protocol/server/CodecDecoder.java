package com.legaoyi.protocol.server;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.gateway.message.handler.DeviceDownMessageDeliverer;
import com.legaoyi.gateway.security.SecurityUtil;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DefaultMessageBuilder;
import com.legaoyi.protocol.util.ServerRuntimeContext;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class CodecDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(CodecDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.isReadable()) {
            int length = in.readableBytes();
            if (length < 2) {
                break;
            }

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

                        logger.error("******异常上行消息，illegal message, message={}", ByteUtils.bytes2hex(bs));
                    }
                } catch (Exception e) {
                    handleIllegalMessage(ctx);

                    logger.error("", e);
                } finally {
                    buf.release();
                }
            } else {
                // 如果终端一直没有发送结束符，消息体超过1024 * 2字节，可能是异常数据或者是服务器受到了攻击，丢弃数据
                if (length >= 2048) {
                    byte[] bs = new byte[length];
                    in.readBytes(bs);

                    handleIllegalMessage(ctx);

                    logger.error("******异常上行消息，illegal message, message too long, message={}", ByteUtils.bytes2hex(bs));
                }
                break;
            }
        }
    }

    private void handleIllegalMessage(ChannelHandlerContext ctx) {
        try {
            ServerRuntimeContext.getBean(DeviceDownMessageDeliverer.class).deliver(ctx, DefaultMessageBuilder.build8001Message(ctx, 2));
        } catch (Exception e) {
            logger.error("******发送应该消息失败", e);
        }
        try {
            // 负载均衡部署时，客户端ip获取不到，获取到的可能是负载均衡服务器的ip，需屏蔽
            String ip = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();
            // 检查流量控制
            SecurityUtil securityUtil = ServerRuntimeContext.getBean(SecurityUtil.class);
            if (!securityUtil.validateByMessageId(ip, "illegal", 3)) {
                ctx.close();
                ServerRuntimeContext.getBean(DataLimitAlarmHandler.class).handleDataLimitAlarm(ip, null, 2);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
