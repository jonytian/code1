package com.legaoyi.file.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.disruptor.DisruptorMessageHandler;

/**
 * disruptor把消息发送mq
 * 
 * @author gaoshengbo
 *
 */
@Component("serverDisruptorMessageHandler")
public class ServerDisruptorMessageHandler implements DisruptorMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerDisruptorMessageHandler.class);

    @Autowired
    @Qualifier("messageProducer")
    private MQMessageProducer producer;

    @Value("${elink.gateway.id}")
    private String gatewayId;

    @Override
    public void handle(final Object message) {
        try {
            String routingKey = null;
            Object data;
            if (message instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) message;
                routingKey = (String) map.get("routingKey");
                data = map.get("message");
                if (data instanceof ExchangeMessage) {
                    ExchangeMessage exchangeMessage = (ExchangeMessage) data;
                    exchangeMessage.setGatewayId(gatewayId);
                }
            } else {
                data = message;
            }
            producer.send(routingKey, data);
        } catch (Exception e) {
            logger.error("******上行消息发送mq失败，send mq message error,message={}", message.toString(), e);
        }
    }
}
