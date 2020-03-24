package com.legaoyi.protocol.server;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.gateway.message.handler.DownstreamMessageDeliverer;
import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8003_MessageBody;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.message.decoder.MessageDecoder;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DefaultMessageBuilder;
import com.legaoyi.protocol.util.SpringBeanUtil;
import com.legaoyi.protocol.util.UnknownMessageBody;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

@ChannelHandler.Sharable
@Component("bytesToMessageChannelHandler")
public class BytesToMessageChannelHandler extends ChannelDuplexHandler {

    private static final Logger logger = LoggerFactory.getLogger(BytesToMessageChannelHandler.class);

    @Value("${message.data.limit}")
    private int messageDataLimit;

    @Value("${defult.protocol.version}")
    private String defultProtocolVersion = Message.DEFULT_PROTOCOL_VERSION;

    @Autowired
    @Qualifier("downstreamMessageDeliverer")
    private DownstreamMessageDeliverer downstreamMessageDeliverer;

    @Autowired
    @Qualifier("packagesCacheManager")
    private PackagesCacheManager packagesCacheManager;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof byte[]) {
            try {
                byte[] bytes = (byte[]) msg;
                Message message = decodeMessage(ctx, bytes);
                if (message != null) {
                    ctx.fireChannelRead(message);
                }
            } catch (Exception e) {
                logger.error("******处理上行消息失败，handlerMessage error,message={}", msg, e);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        } else {
            ReferenceCountUtil.release(msg);
        }
    }

    private Message decodeMessage(ChannelHandlerContext ctx, byte[] bytes) {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();

        Message message = new Message();
        try {
            MessageDecoder decoder = new MessageDecoder();
            decoder.decode(bytes);
            byte[] messageBody = decoder.getMessageBody();
            MessageHeader messageHeader = decoder.getMessageHeader();
            if (messageHeader.getIsSubpackage()) {
                messageBody = checkPackages(session, messageHeader, messageBody);
                if (messageBody == null) {// 说明存在分包未接收，暂时不做处理
                    return null;
                }
            }

            message.setMessageHeader(messageHeader);
            message.setLength(bytes.length);
            String messageId = messageHeader.getMessageId();
            if (messageBody != null && messageBody.length > 0) {
                String protocolVersion = session.getProtocolVersion();
                if (StringUtils.isBlank(protocolVersion)) {
                    protocolVersion = defultProtocolVersion;
                }
                try {
                    MessageBodyDecoder messageBodyDecoder = SpringBeanUtil.getMessageBodyDecoder(messageId, protocolVersion);
                    message.setMessageBody(messageBodyDecoder.decode(messageBody));
                } catch (Exception e) {
                    try {
                        downstreamMessageDeliverer.deliver(session, DefaultMessageBuilder.build8001Message(message, 3));
                    } catch (Exception ee) {
                        logger.error("******发送应该消息失败", ee);
                    }
                    // 消息头已解析完成，但消息体是错误消息或者是扩展消息
                    String messageBodyStr = ByteUtils.bytes2hex(messageBody);
                    UnknownMessageBody unknownMessageBody = new UnknownMessageBody();
                    unknownMessageBody.setMessageBody(messageBodyStr);
                    unknownMessageBody.setMessageId(messageHeader.getMessageId());
                    messageHeader.setMessageId("unknown");
                    message.setMessageBody(unknownMessageBody);

                    logger.error("******解码上行消息失败，unknown messageBody,messageBody={}", messageBodyStr, e);
                }
            }
        } catch (Exception e) {
            message = null;

            logger.error("******解码上行消息失败，decode message error,message={}", ByteUtils.bytes2hex(bytes), e);
            try {
                downstreamMessageDeliverer.deliver(session, DefaultMessageBuilder.build8001Message(ctx, 2));
            } catch (Exception ee) {
                logger.error("******发送应该消息失败", e);
            }
        }
        return message;
    }

    private byte[] checkPackages(Session session, MessageHeader messageHeader, byte[] messageBody) throws Exception {
        // 这里可以限制分包数，防止恶意攻击发送超大数据包撑爆缓存，todo
        if (logger.isInfoEnabled()) {
            logger.info("******分包,message packages,messageId={},packageSeq={}", messageHeader.getMessageId(), messageHeader.getPackageSeq());
        }

        String cacheKey = messageHeader.getSimCode().concat("_").concat(messageHeader.getMessageId());
        int totalPackage = messageHeader.getTotalPackage();
        int packageSeq = messageHeader.getPackageSeq();
        Packages packages = packagesCacheManager.addPackageCache(cacheKey, totalPackage, packageSeq, messageBody);
        if (packages.isRetransmission() && packages.isFull()) { // 如果是重传包
            packagesCacheManager.removePackageCache(cacheKey);
            return packages.getAllPackages();
        }

        boolean checkPackage = false;
        if (packageSeq >= totalPackage) {// 如果是最后一个包
            if (packages.isFull()) {
                packagesCacheManager.removePackageCache(cacheKey);
                return packages.getAllPackages();
            } else {
                checkPackage = true;
            }
        } else {
            if (packageSeq % 100 == 0) {
                checkPackage = true;
            }
        }

        Message message = new Message();
        message.setMessageHeader(messageHeader);

        if (checkPackage) {
            // 存在未接收的包,发送消息终端重发
            List<Integer> packageIds = packages.getLostPackageSeqs();
            if (packageIds != null) {
                int percent = packageIds.size() * 100 / packageSeq;
                logger.error("******设备上传的数据存在丢包,simCode={},percent={},packageIds={}", messageHeader.getSimCode(), percent + "%", JsonUtil.covertObjectToString(packageIds));
                try {
                    String protocolVersion = session.getProtocolVersion();
                    if (protocolVersion != null && !protocolVersion.equals(Message.DEFULT_PROTOCOL_VERSION)) {
                        downstreamMessageDeliverer.deliver(session, build8003Message(messageHeader, packageIds, packages.getFirstSeq()));
                    } else {
                        // 如果是2011版本协议，则告诉终端设备接收数据失败
                        downstreamMessageDeliverer.deliver(session, DefaultMessageBuilder.build8001Message(message, 1));
                        return null;
                    }
                } catch (Exception e) {
                    logger.error("******发送分包补传失败", e);
                }
            }
        }

        try {
            downstreamMessageDeliverer.deliver(session, DefaultMessageBuilder.build8001Message(message, 0));
        } catch (Exception e) {
            logger.error("******发送应答消息失败", e);
        }
        return null;
    }

    private Message build8003Message(MessageHeader messageHeader, List<Integer> packageIds, int messageSeq) {
        messageHeader.setIsSubpackage(false);
        messageHeader.setMessageId(Jt808_2019_8003_MessageBody.MESSAGE_ID);

        Jt808_2019_8003_MessageBody messageBody = new Jt808_2019_8003_MessageBody();
        messageBody.setMessageSeq(messageSeq);
        messageBody.setPackageIds(packageIds);

        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        return message;
    }
}
