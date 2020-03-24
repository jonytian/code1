package com.legaoyi.file.message.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.file.messagebody.Tjsatl_2017_1211_MessageBody;
import com.legaoyi.file.server.ServerMessageHandler;
import com.legaoyi.file.server.security.SecurityUtil;
import com.legaoyi.file.server.util.Constants;
import com.legaoyi.file.server.util.DefaultMessageBuilder;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.util.ServerRuntimeContext;

import io.netty.channel.ChannelHandlerContext;

@Component("tjsatl_1211_MessageHandler")
public class Tjsatl_2017_1211_MessageHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Tjsatl_2017_1211_MessageHandler.class);

    @Autowired
    @Qualifier("deviceDownMessageDeliverer")
    private DeviceDownMessageDeliverer messageDeliverer;

    @Autowired
    @Qualifier("serverMessageHandler")
    private ServerMessageHandler serverMessageHandler;

    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws Exception {
        String sessionState = ctx.channel().attr(Constants.ATTRIBUTE_SESSION_STATE).get();
        if (StringUtils.isEmpty(sessionState)) {
            handleIllegalMessage(ctx, message, 2);
            return;
        }

        // 设置附件名称
        Tjsatl_2017_1211_MessageBody messageBody = (Tjsatl_2017_1211_MessageBody) message.getMessageBody();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("simCode", message.getMessageHeader().getSimCode());
        map.put("fileName", messageBody.getFileName());
        map.put("fileSize", messageBody.getFileSize());
        ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT_INFO).set(map);

        serverMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE, message.clone(), message.getMessageHeader().getMessageSeq() + ""));

        // 应答8001消息
        messageDeliverer.deliver(ctx, DefaultMessageBuilder.build8001Message(message, 0));
    }

    private void handleIllegalMessage(ChannelHandlerContext ctx, Message message, int result) {
        try {
            messageDeliverer.deliver(ctx, DefaultMessageBuilder.build8001Message(message, result));
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
