package com.legaoyi.file.server;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.file.message.codec.MessageDecoder;
import com.legaoyi.file.message.handler.DeviceDownMessageDeliverer;
import com.legaoyi.file.message.handler.MessageHandler;
import com.legaoyi.file.messagebody.Attachment;
import com.legaoyi.file.server.security.SecurityUtil;
import com.legaoyi.file.server.util.Constants;
import com.legaoyi.file.server.util.DefaultMessageBuilder;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.ServerRuntimeContext;
import com.legaoyi.protocol.util.SpringBeanUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

@ChannelHandler.Sharable
@Component("bytesToMessageChannelHandler")
public class BytesToMessageChannelHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(BytesToMessageChannelHandler.class);

    @Autowired
    @Qualifier("deviceDownMessageDeliverer")
    private DeviceDownMessageDeliverer messageDeliverer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof Attachment) {
                Attachment attachment = (Attachment) msg;
                Map<String, Object> attachmentMap = ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT_INFO).get();
                if (attachmentMap == null || !attachment.getFileName().equals((String) attachmentMap.get("fileName"))) {
                    // 未按标准流程上传文件，视为服务器受到非法攻击，放入黑名单
                    handleIllegalMessage(ctx, 3);
                    logger.error("******illegal attachment,fileName={},lastFileName={}", attachment.getFileName(), attachmentMap.get("fileName"));
                    return;
                }

                List<Attachment> attachmentList = ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT).get();
                if (attachmentList == null) {
                    attachmentList = new ArrayList<Attachment>();
                    ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT).set(attachmentList);
                }
                attachmentList.add(attachment);
            } else if (msg instanceof byte[]) {
                Message message = decodeMessage(ctx, (byte[]) msg);
                try {
                    ServerRuntimeContext.getBean("tjsatl_" + message.getMessageHeader().getMessageId() + "_MessageHandler", MessageHandler.class).handle(ctx, message);
                } catch (Exception e) {
                    handleIllegalMessage(ctx, 3);
                    logger.error("******illegal message,message={}", message, e);
                }
            }
        } catch (Exception e) {
            logger.error("******handler attachment data error,message={}", msg, e);
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private Message decodeMessage(ChannelHandlerContext ctx, byte[] bytes) {
        Message message = new Message();
        try {
            MessageDecoder decoder = new MessageDecoder();
            decoder.decode(bytes);
            byte[] messageBody = decoder.getMessageBody();
            MessageHeader messageHeader = decoder.getMessageHeader();
            message.setMessageHeader(messageHeader);
            message.setLength(bytes.length);
            String messageId = messageHeader.getMessageId();
            if (messageBody != null && messageBody.length > 0) {
                try {
                    MessageBodyDecoder messageBodyDecoder = SpringBeanUtil.getMessageBodyDecoder(messageId, Constants.PROTOCOL_VERSION);
                    message.setMessageBody(messageBodyDecoder.decode(messageBody));
                } catch (Exception e) {
                    handleIllegalMessage(ctx, 3);
                    logger.error("******解码上行消息失败，decode message error,message={}", ByteUtils.bytes2hex(bytes), e);
                }
            }
        } catch (Exception e) {
            message = null;
            handleIllegalMessage(ctx, 2);
            logger.error("******解码上行消息失败，decode message error,message={}", ByteUtils.bytes2hex(bytes), e);
        }
        return message;
    }

    private void handleIllegalMessage(ChannelHandlerContext ctx, int result) {
        try {
            messageDeliverer.deliver(ctx, DefaultMessageBuilder.build8001Message(result));
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
}
