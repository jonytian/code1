package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.common.message.ExchangeMessage;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0100_2011"
		+ Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0100_2011_MessageHandler extends MessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(JTT808_0100_2011_MessageHandler.class);

	@Autowired
	@Qualifier("messageBatchSaveProducer")
	private DisruptorEventBatchProducer messageBatchSaveProducer;

	@Autowired
	public JTT808_0100_2011_MessageHandler(@Qualifier("deviceDownMessageSendHandler") MessageHandler handler) {
		setSuccessor(handler);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handle(ExchangeMessage message) throws Exception {
		Map<?, ?> map = (Map<?, ?>) message.getMessage();
		Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
		Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

		String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);
		// String terminalId = (String) messageBody.get("terminalId");

		Map<String, Object> respMessageBody = new HashMap<String, Object>();
		respMessageBody.put(Constants.MAP_KEY_MESSAGE_SEQ, messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ));

		// 由于各个设备厂商对终端id理解不一样，暂时只校验sim卡号
		Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
		int result = 4;
		if (device != null) {
			// 设备状态,0:未注册；1:已注册；2:在线；3:离线;4:已注销；5：已停用
			Integer state = (Integer) device.get("state");
			// 允许重复注册
			// if (state != null && (state == 1 || state == 2 || state == 3)) {
			// result = 3;
			// } else

			if (state != null && state == 5) {// || !terminalId.equals((String) device.get("terminalId"))) {
				result = 4;
			} else {
				Map<String, Object> updateInfo = new HashMap<String, Object>();
				updateInfo.put(Constants.MAP_KEY_SIM_CODE, simCode);
				updateInfo.put("type", Constants.DEVICE_STATE_BATCH_SAVE_TYPE_REGISTERED);
				respMessageBody.put("authCode", device.get("authCode"));
				result = 0;
				messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_DEVICE_STATE, updateInfo));
			}
		}

		if (result != 0) {
			logger.info("register failure, message={}, deviceInfo={}", message, device);
			messageBody.put("desc", "注册失败");
			messageBody.put("result", result);
		} else {
			messageBody.put("desc", "注册成功");
		}
		respMessageBody.put("result", result);

		// 响应终端
		Map<String, Object> respMessageHeader = new HashMap<String, Object>();
		respMessageHeader.put(Constants.MAP_KEY_SIM_CODE, simCode);
		respMessageHeader.put(Constants.MAP_KEY_MESSAGE_ID, "8100");
		Map<String, Object> downMessage = new HashMap<String, Object>();
		downMessage.put(Constants.MAP_KEY_MESSAGE_HEADER, respMessageHeader);
		downMessage.put(Constants.MAP_KEY_MESSAGE_BODY, respMessageBody);
		// 注入下发消息处理链
		ExchangeMessage exchangeMessage = new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE,
				downMessage, null, message.getGatewayId());
		exchangeMessage.setExtAttribute(message.getExtAttribute());
		getSuccessor().handle(exchangeMessage);
	}
}
