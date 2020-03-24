package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.WebJmsMessage;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.handler.MessageHandler;

/**
 *
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011_notify" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0801_2011_NotifyHandler extends MessageHandler {

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

        Map<String, Object> webJmsMessage = new HashMap<String, Object>();
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        webJmsMessage.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        webJmsMessage.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
        webJmsMessage.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
        webJmsMessage.put("mediaType", messageBody.get("mediaType"));
        webJmsMessage.put("channelId", messageBody.get("channelId"));
        webJmsMessage.put("filePath", messageBody.get("filePath"));
        webJmsMessage.put("id", messageBody.get("_id"));
        platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.MEDIA_MESSAGE_TYPE, webJmsMessage).toString());
    }

}
