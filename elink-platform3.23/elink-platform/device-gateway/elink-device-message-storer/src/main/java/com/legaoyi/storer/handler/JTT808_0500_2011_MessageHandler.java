package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.common.message.ExchangeMessage;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0500_2011"
		+ Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0500_2011_MessageHandler extends MessageHandler {

	@Autowired
	public JTT808_0500_2011_MessageHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011"
			+ Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
		setSuccessor(handler);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(ExchangeMessage message) throws Exception {
		// 0500消息也更新消息缓存
		ExchangeMessage message1 = new ExchangeMessage();
		message1.setExchangeId(message.getExchangeId());
		message1.setGatewayId(message.getGatewayId());
		message1.setMessageId(message.getMessageId());
		message1.setExtAttribute(message.getExtAttribute());
		Map<String, Object> messageData1 = new HashMap<String, Object>();
		Map<String, Object> messageHeader1 = new HashMap<String, Object>();
		Map<String, Object> messageBody1 = new HashMap<String, Object>();

		Map<?, ?> map = (Map<?, ?>) message.getMessage();
		Map<String, Object> messageHeader = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
		messageHeader1.putAll(messageHeader);
		Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
		messageBody1.putAll((Map<String, Object>) messageBody.get("gpsInfo"));
		// 此消息也要保存gps历史轨迹中
		messageHeader1.put(Constants.MAP_KEY_MESSAGE_ID, "0200");
		messageData1.put(Constants.MAP_KEY_MESSAGE_HEADER, messageHeader1);
		messageData1.put(Constants.MAP_KEY_MESSAGE_BODY, messageBody1);
		message1.setMessage(messageData1);
		getSuccessor().handle(message1);
	}

}
