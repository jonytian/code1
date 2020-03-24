package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.dao.DeviceUpMessageDao;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.WebJmsMessage;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1206_2016" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_1206_2016_MessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Autowired
    @Qualifier("deviceUpMessageDao")
    private DeviceUpMessageDao deviceUpMessageDao;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        String enterpriseId = (String) device.get(Constants.MAP_KEY_ENTERPRISE_ID);
        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
        
        Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        int messageSeq = (Integer) messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ);
        int respMessageSeq = (Integer) messageBody.get(Constants.MAP_KEY_MESSAGE_SEQ);
        int result = (Integer) messageBody.get("result");
        deviceUpMessageDao.updateFileUploadState(deviceId, respMessageSeq, result + 3,messageSeq);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
        data.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
        data.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
        if (carId != null) {
            data.put(Constants.MAP_KEY_CAR_ID, carId);
            data.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
        }
        data.putAll(messageBody);

        // 消息推送到web页面
        platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.FILE_UPLOAD_MESSAGE_TYPE, data).toString());
    }
}
