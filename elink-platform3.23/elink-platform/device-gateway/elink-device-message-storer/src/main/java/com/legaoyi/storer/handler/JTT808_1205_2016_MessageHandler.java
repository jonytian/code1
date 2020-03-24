package com.legaoyi.storer.handler;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.dao.DeviceUpMessageDao;
import com.legaoyi.storer.util.Constants;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1205_2016" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_1205_2016_MessageHandler extends MessageHandler {

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

        Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        int messageSeq = (Integer) messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ);
        int respMessageSeq = (Integer) messageBody.get(Constants.MAP_KEY_MESSAGE_SEQ);
        deviceUpMessageDao.updateFileUploadState(deviceId, respMessageSeq, 3, messageSeq);
    }
}
