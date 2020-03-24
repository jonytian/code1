package com.legaoyi.rabbitmq;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.mq.MQMessageHandler;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.exception.MessageDeliveryException;
import com.legaoyi.protocol.exception.UnsupportedMessageException;
import com.legaoyi.protocol.server.ServerMessageHandler;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.gateway.message.handler.ExchangeMessageHandler;
import com.legaoyi.common.message.ExchangeMessage;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
public class DownstreamMqMessageHandler implements MQMessageHandler {

    private final static Logger logger = LoggerFactory.getLogger(DownstreamMqMessageHandler.class);

    private ServerMessageHandler urgentMessageHandler;

    private List<ExchangeMessageHandler> messageHandleList;

    public void setUrgentMessageHandler(ServerMessageHandler urgentMessageHandler) {
        this.urgentMessageHandler = urgentMessageHandler;
    }

    public void setMessageHandleList(List<ExchangeMessageHandler> messageHandleList) {
        this.messageHandleList = messageHandleList;
        if (messageHandleList != null && !messageHandleList.isEmpty()) {
            Iterator<ExchangeMessageHandler> it = messageHandleList.iterator();
            ExchangeMessageHandler parser = it.next();
            while (it.hasNext()) {
                ExchangeMessageHandler parser1 = it.next();
                parser.setSuccessor(parser1);
                parser = parser1;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(String message) throws Exception {
        String exchangeId = null;
        int result = 0;
        String desc = "";
        ExchangeMessage exchangeMessage = null;
        try {
            // 消息解析，消息路由，消息下发，消息响应
            exchangeMessage = JsonUtil.convertStringToObject(message, ExchangeMessage.class);
            exchangeId = exchangeMessage.getExchangeId();
            if (messageHandleList != null && !messageHandleList.isEmpty()) {
                messageHandleList.get(0).handle(exchangeMessage);
            }
        } catch (MessageDeliveryException e) {
            result = 1;
            desc = e.getMessage();
            logger.error("******发送下行消息错误，handle message error,message={}", message, desc, e);
        } catch (IllegalMessageException e) {
            result = 2;
            desc = e.getMessage();
            logger.error("******发送下行消息错误，handle message error,message={}", message, desc, e);
        } catch (UnsupportedMessageException e) {
            result = 3;
            desc = e.getMessage();
            logger.error("******发送下行消息错误，handle message error,message={}", message, desc, e);
        } catch (Exception e) {
            result = -1;
            desc = e.getMessage();
            logger.error("******发送下行消息错误，handle message error,message={}", message, desc, e);
        } finally {
            if (!StringUtils.isEmpty(exchangeId)) {
                String messageId = null;
                String simCode = null;
                if (exchangeMessage != null) {
                    Map<String, Object> map = (Map<String, Object>) exchangeMessage.getMessage();
                    Map<String, Object> messageHeader = (Map<String, Object>) map.get("messageHeader");
                    if (messageHeader != null) {
                        messageId = (String) messageHeader.get("messageId");
                        simCode = (String) messageHeader.get("simCode");
                    }
                }
                Map<String, Object> resp = new HashMap<String, Object>();
                resp.put("simCode", simCode);
                resp.put("messageId", messageId);
                resp.put("result", result);
                resp.put("desc", desc);
                urgentMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_RESP_MESSAGE, resp, exchangeId));
            }
        }
    }

    @Override
    public void handle(byte[] bytes) throws Exception {}
}
