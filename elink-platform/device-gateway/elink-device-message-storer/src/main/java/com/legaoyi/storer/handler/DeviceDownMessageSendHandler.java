package com.legaoyi.storer.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

/**
 * 808消息下发
 * 
 * @author gaoshengbo
 *
 */
@Component("deviceDownMessageSendHandler")
public class DeviceDownMessageSendHandler extends MessageHandler {

    @Autowired
    @Qualifier("commonDownMessageProducer")
    private MQMessageProducer producer;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    private static final long MAX_MESSAGE_SEQ = 65535;

    @Autowired
    public DeviceDownMessageSendHandler(@Qualifier("deviceDownMessageSaveHandler") MessageHandler handler) {
        setSuccessor(handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        if (messageHeader != null) {
            String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);
            int messageSeq = this.redisService.generateSeq(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_DOWN_MESSAGE_SEQ_CACHE_PREFIX.concat(simCode), MAX_MESSAGE_SEQ);
            messageHeader.put("messageSeq", messageSeq);
        }
        producer.send(message.getGatewayId(), message);
        if (this.getSuccessor() != null) {
            this.getSuccessor().handle(message);
        }
    }

}
