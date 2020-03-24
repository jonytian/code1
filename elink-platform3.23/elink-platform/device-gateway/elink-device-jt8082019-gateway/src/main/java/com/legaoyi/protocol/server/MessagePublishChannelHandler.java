package com.legaoyi.protocol.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.protocol.message.Message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
@Component("messagePublishChannelHandler")
public class MessagePublishChannelHandler extends BaseMessageChannelInboundHandler {

    @Value("${urgent.queue.message}")
    private String urgentMessage;

    private static Map<String, Object> urgentMessageMap;

    @Value("${media.queue.message}")
    private String mediaMessage;

    private static Map<String, Object> mediaMessageMap;

    private static Map<String, Object> ignoreMessageMap;

    @Autowired
    @Qualifier("urgentUpstreamMessageHandler")
    private ServerMessageHandler urgentUpstreamMessageHandler;

    @Autowired
    @Qualifier("commonUpstreamMessageHandler")
    private ServerMessageHandler commonUpstreamMessageHandler;

    @Autowired
    @Qualifier("mediaUpstreamMessageHandler")
    private ServerMessageHandler mediaUpstreamMessageHandler;

    @Value("${gateway.ignore.message}")
    private String ignoreMessage;

    @PostConstruct
    public void init() {
        urgentMessageMap = new HashMap<String, Object>();
        if (!StringUtils.isBlank(urgentMessage)) {
            String[] arr = urgentMessage.split(",");
            for (String messageId : arr) {
                urgentMessageMap.put(messageId, null);
            }
            urgentMessage = null;
        }

        mediaMessageMap = new HashMap<String, Object>();
        if (!StringUtils.isBlank(mediaMessage)) {
            String[] arr = mediaMessage.split(",");
            for (String messageId : arr) {
                mediaMessageMap.put(messageId, null);
            }
            mediaMessage = null;
        }

        ignoreMessageMap = new HashMap<String, Object>();
        if (!StringUtils.isBlank(ignoreMessage)) {
            String[] arr = ignoreMessage.split(",");
            for (String messageId : arr) {
                ignoreMessageMap.put(messageId, null);
            }
            ignoreMessage = null;
        }
    }

    @Override
    protected boolean handle(ChannelHandlerContext ctx, Message message) {
        String messageId = message.getMessageHeader().getMessageId();
        if (ignoreMessageMap.containsKey(messageId)) {
            logger.info("******该消息被网关丢弃，不发送业务平台", message);
            return true;
        }
        try {
            String exchangeId = String.valueOf(message.getMessageHeader().getMessageSeq());
            if (urgentMessageMap.containsKey(messageId)) {
                urgentUpstreamMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE, message.clone(), exchangeId));
            } else if (mediaMessageMap.containsKey(messageId)) {
                mediaUpstreamMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MEDIA_MESSAGE, message.clone(), exchangeId));
            } else {
                commonUpstreamMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE, message.clone(), exchangeId));
            }
        } catch (Exception e) {
            logger.error("******上行消息发送mq失败，send message to mq error,message={}", message, e);
        }
        return true;
    }
}
