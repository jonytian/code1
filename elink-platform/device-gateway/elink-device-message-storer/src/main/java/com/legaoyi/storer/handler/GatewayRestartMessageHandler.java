package com.legaoyi.storer.handler;

import com.legaoyi.common.message.ExchangeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.common.util.Constants;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.WebJmsMessage;

@Component("gatewayRestartMessageHandler")
public class GatewayRestartMessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_RESTART)) {
            // 更新缓存中设备的状态
            String gatewayId = message.getGatewayId();
            redisService.del(Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId));
            deviceService.setDeviceStateOffline(gatewayId);

            // 网关重启消息推送到web页面
            platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.JTT808_GATEWAY_RESTART, message.getMessage()).toString());
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
