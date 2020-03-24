package com.legaoyi.protocol.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.gateway.message.handler.DeviceDownMessageDeliverer;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.util.DefaultMessageBuilder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
@Component("autoWrite8001MessageChannelHandler")
public class AutoWrite8001MessageChannelHandler extends BaseMessageChannelInboundHandler {

    @Value("${auto.response.8001.message}")
    private String autoResp8001Messages;

    private static Map<String, Object> autoResp8001MessagesMap;

    @Autowired
    @Qualifier("deviceDownMessageDeliverer")
    private DeviceDownMessageDeliverer messageDeliverer;

    @PostConstruct
    public void init() {
        autoResp8001MessagesMap = new HashMap<String, Object>();
        if (!StringUtils.isBlank(autoResp8001Messages)) {
            String[] arr = autoResp8001Messages.split(",");
            for (String messageId : arr) {
                autoResp8001MessagesMap.put(messageId, null);
            }
            autoResp8001Messages = null;
        }
    }

    @Override
    protected boolean handle(ChannelHandlerContext ctx, Message message) {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();
        String messageId = message.getMessageHeader().getMessageId();
        if (autoResp8001MessagesMap.containsKey(messageId)) {
            try {
                Message msg = DefaultMessageBuilder.build8001Message(message, 0);
                messageDeliverer.deliver(session, msg);
            } catch (Exception e) {
                logger.error("******发送通用应答消息失败，response 8001 message error,message={}", message, e);
            }
        }
        return false;
    }

}
