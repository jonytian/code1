package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.dao.DeviceDownMessageDao;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.JsonUtil;

/**
 * 808离线消息下发
 * 
 * @author gaoshengbo
 *
 */
@Component("deviceOfflineDownMessageHandler")
public class DeviceOfflineDownMessageHandler implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(DeviceOfflineDownMessageHandler.class);

    @Autowired
    @Qualifier("deviceDownMessageDao")
    private DeviceDownMessageDao deviceDownMessageDao;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("deviceDownMessageSendHandler")
    private MessageHandler deviceDownMessageSendHandler;

    @SuppressWarnings("unchecked")
    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof OnlineNotifyHandler)) {
            return;
        }
        try {
            ExchangeMessage exchangeMessage = (ExchangeMessage) arg;
            Map<String, Object> message = (Map<String, Object>) exchangeMessage.getMessage();
            String simCode = (String) message.get(Constants.MAP_KEY_SIM_CODE);
            if (simCode != null && redisService.sRem(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_COMMAND_CACHE, simCode)) {
                Map<?, ?> device = (Map<?, ?>) exchangeMessage.getExtAttribute(Constants.MAP_KEY_DEVICE);
                String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
                // 查询离线下行消息
                List<?> list = deviceDownMessageDao.getOfflineMessage(deviceId);
                if (list != null && !list.isEmpty()) {
                    Map<String, Object> messageHeader = new HashMap<String, Object>();
                    messageHeader.put(Constants.MAP_KEY_SIM_CODE, simCode);
                    Map<String, Object> downMessage = new HashMap<String, Object>();
                    downMessage.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader);
                    for (Object o1 : list) {
                        Map<?, ?> map = (Map<?, ?>) o1;
                        String messageId = (String) map.get(Constants.MAP_KEY_MESSAGE_ID);
                        String messageBody = (String) map.get(Constants.MAP_KEY_MESSAGE_BODY);
                        messageHeader.put(Constants.MAP_KEY_MESSAGE_ID, messageId);
                        downMessage.put(Constants.MAP_KEY_MESSAGE_BODY, JsonUtil.convertStringToObject(messageBody, Map.class));
                        ExchangeMessage exchangeMessage1 = new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE, downMessage, null, exchangeMessage.getGatewayId());
                        exchangeMessage1.setExtAttribute(exchangeMessage.getExtAttribute());
                        deviceDownMessageSendHandler.handle(exchangeMessage1);
                        deviceDownMessageDao.delMessage(String.valueOf(map.get("id")));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("******deviceOfflineDownMessageHandler error", e);
        }
    }

}
