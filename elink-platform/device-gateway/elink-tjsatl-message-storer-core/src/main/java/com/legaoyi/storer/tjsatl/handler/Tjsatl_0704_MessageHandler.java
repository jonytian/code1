package com.legaoyi.storer.tjsatl.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.tjsatl.service.FileServerService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0704_tjsatl"
		+ Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Tjsatl_0704_MessageHandler extends MessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(Tjsatl_0704_MessageHandler.class);

	@Autowired
	@Qualifier("redisService")
	private RedisService<?> redisService;

	@Autowired
	@Qualifier("fileServerService")
	private FileServerService fileServerService;

	@Autowired
	@Qualifier("deviceDownMessageSendHandler")
	private MessageHandler deviceDownMessageSendHandler;

	@SuppressWarnings("unchecked")
	@Override
	public void handle(ExchangeMessage message) throws Exception {
		Map<?, ?> map = (Map<?, ?>) message.getMessage();
		Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
		List<?> gpsInfoList = (List<?>) messageBody.get("gpsInfoList");
		for (Object o : gpsInfoList) {
			Map<String, Object> gpsInfo = (Map<String, Object>) o;

			String id = IdGenerator.nextIdStr();
			gpsInfo.put("id", id);

			// 告警与位置信息关联
			Map<String, Object> otherAlarm = new HashMap<String, Object>();

			/** 高级驾驶辅助系统报警信息 **/
			Map<String, Object> adasAlarm = (Map<String, Object>) gpsInfo.get("adasAlarm");
			boolean attachment = false;
			if (adasAlarm != null) {
				Integer type = (Integer) adasAlarm.get("type");
				otherAlarm.put("adas" + type, 1);
				otherAlarm.put("adas", type);
				attachment = checkAttachment(message, adasAlarm, id);
			}

			/** 驾驶员状态监测系统报警信息 **/
			Map<String, Object> dsmAlarm = (Map<String, Object>) gpsInfo.get("dsmAlarm");
			if (dsmAlarm != null) {
				Integer type = (Integer) dsmAlarm.get("type");
				otherAlarm.put("dsm" + type, 1);
				otherAlarm.put("dsm", type);
				attachment = checkAttachment(message, dsmAlarm, id);
			}

			/** 胎压监测系统报警信息 **/
			Map<String, Object> tpmAlarm = (Map<String, Object>) gpsInfo.get("tpmAlarm");
			if (tpmAlarm != null && tpmAlarm.get("alarmList") != null) {
				List<?> alarmList = (List<?>) tpmAlarm.get("alarmList");
				for (Object o1 : alarmList) {
					Map<?, ?> m = (Map<?, ?>) o1;
					// 11111110=254
					Integer type = (Integer) m.get("type");
					if (type != null && (type & 254) > 0) {
						otherAlarm.put("tpm" + m.get("seq"), type);
					}
				}
				otherAlarm.put("tpm", 1);
				attachment = checkAttachment(message, tpmAlarm, id);
			}

			/** 盲区监测系统报警信息 **/
			Map<String, Object> bsdAlarm = (Map<String, Object>) gpsInfo.get("bsdAlarm");
			if (bsdAlarm != null) {
				Integer type = (Integer) bsdAlarm.get("type");
				otherAlarm.put("bsd" + type, 1);
				otherAlarm.put("bsd", type);
				attachment = checkAttachment(message, bsdAlarm, id);
			}

			if (!otherAlarm.isEmpty()) {
				if (attachment) {
					otherAlarm.put("att", 1);
				}
				// 苏标告警类型
				otherAlarm.put("type", Constants.AlarmType.TJSATL.getType());
				gpsInfo.put("otherAlarm", otherAlarm);
			}
		}
	}

	// 是否有报警附件
	private boolean checkAttachment(ExchangeMessage message, Map<String, Object> alarm, String alarmId) {
		Integer totalFile = (Integer) alarm.get("totalFile");
		if (totalFile != null && totalFile > 0) {
			try {
				Map<String, Object> fileServerInfo = getFileServer();
				// 下发上传报警附件指令
				Map<?, ?> map = (Map<?, ?>) message.getMessage();
				Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
				String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);

				Map<String, Object> respMessageHeader = new HashMap<String, Object>();
				respMessageHeader.put(Constants.MAP_KEY_SIM_CODE, simCode);
				respMessageHeader.put(Constants.MAP_KEY_MESSAGE_ID, "9208");

				Map<String, Object> respMessageBody = new HashMap<String, Object>();
				respMessageBody.putAll(fileServerInfo);
				respMessageBody.put("terminalId", alarm.get("terminalId"));
				respMessageBody.put("alarmTime", alarm.get("alarmTime"));
				respMessageBody.put("alarmSeq", alarm.get("alarmSeq"));
				respMessageBody.put("alarmExt", alarm.get("alarmExt"));
				respMessageBody.put("totalFile", totalFile);
				respMessageBody.put("alarmId", alarmId);

				Map<String, Object> downMessage = new HashMap<String, Object>();
				downMessage.put(Constants.MAP_KEY_MESSAGE_HEADER, respMessageHeader);
				downMessage.put(Constants.MAP_KEY_MESSAGE_BODY, respMessageBody);
				// 注入下发消息处理链
				ExchangeMessage exchangeMessage = new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE,
						downMessage, null, message.getGatewayId());
				exchangeMessage.setExtAttribute(message.getExtAttribute());
				deviceDownMessageSendHandler.handle(exchangeMessage);
				return true;
			} catch (Exception e) {
				logger.info("*********send 9208 error", e);
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getFileServer() throws Exception {
		List<?> list = fileServerService.getFileServer();
		Map<String, Object> data = null;
		long max = 0;// 获得最少连接数的流媒体服务器,可实现软负载均衡
		String gatewayId = null;
		for (Object o : list) {
			Map<String, Object> map = (Map<String, Object>) o;
			gatewayId = (String) map.get("gatewayId");
			int count = redisService.getInt(gatewayId);
			if (count <= 0) {
				data = map;
				break;
			}
			int limit = (Integer) map.get("limit");
			if (max > count && limit >= count) {
				data = map;
			} else {
				if (max < count) {
					max = count;
					data = map;
				}
			}
		}
		if (data == null) {
			throw new BizProcessException("无可用附件服务器，请联系系统管理员进行配置！");
		}
		redisService.incr(gatewayId);
		return data;
	}

}
