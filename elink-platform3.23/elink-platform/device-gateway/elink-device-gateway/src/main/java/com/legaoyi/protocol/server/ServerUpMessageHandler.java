package com.legaoyi.protocol.server;

import java.util.HashMap;
import java.util.Map;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.disruptor.DisruptorEventProducer;

/**
 * disruptor处理消息
 * 
 * @author gaoshengbo
 *
 */
public class ServerUpMessageHandler implements ServerMessageHandler {

    private DisruptorEventProducer producer;

    private String routingKey;

    public void setProducer(DisruptorEventProducer producer) {
        this.producer = producer;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("routingKey", routingKey);
        data.put("message", message);
        producer.produce(data);
    }

}
