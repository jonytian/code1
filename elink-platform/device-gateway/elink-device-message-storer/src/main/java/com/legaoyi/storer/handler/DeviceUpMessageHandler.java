package com.legaoyi.storer.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.ServerRuntimeContext;
import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.DateUtils;

/**
 * 
 * 终端上行消息处理入口
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component("deviceUpMessageHandler")
public class DeviceUpMessageHandler extends MessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(DeviceUpMessageHandler.class);

	private static final String DEFULT_PROTOCOL_VERSION = "2011";

	@Autowired
	@Qualifier("deviceService")
	private DeviceService deviceService;

	@Autowired
	@Qualifier("messageBatchSaveProducer")
	private DisruptorEventBatchProducer messageBatchSaveProducer;

	@Value("${batchSave.threadPool.size}")
	private int threadPoolSize = 5;

	@Autowired
	@Qualifier("cacheManager")
	private CacheManager cacheManager;

	private ExecutorService fixedThreadPool = null;

	@PostConstruct
	public void init() {
		fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
	}

	@Override
	public void handle(ExchangeMessage message) throws Exception {
		if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE)
				|| message.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_UP_MEDIA_MESSAGE)) {
			fixedThreadPool.execute(new Runnable() {

				@SuppressWarnings("unchecked")
				public void run() {
					try {
						Map<?, ?> map = (Map<?, ?>) message.getMessage();
						Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
						String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
						String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);
						Map<String, Object> device = deviceService.getDeviceInfo(simCode);
						String protocolVersion = null;
						if (device != null) {
							message.putExtAttribute(Constants.MAP_KEY_DEVICE, device);
							protocolVersion = (String) device.get("protocolVersion");
						} else {
							protocolVersion = DEFULT_PROTOCOL_VERSION;
						}

						try {
							MessageHandler messageHandler = ServerRuntimeContext.getBean(
									Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(messageId).concat("_")
											.concat(protocolVersion)
											.concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX),
									MessageHandler.class);
							messageHandler.handle(message);
						} catch (NoSuchBeanDefinitionException e) {

						} catch (Exception e) {
							logger.error(" handler message error,message={}", message, e);
						}

						if (device != null) {
							// 保存消息
							messageBatchSaveProducer
									.produce(new BatchMessage(Constants.BATCH_MESSAGE_DEVICE_UP_MESSAGE, message));

							// 流量统计
							Integer length = (Integer) map.get("length");
							if (length != null) {
								updateDataCount((String) device.get(Constants.MAP_KEY_DEVICE_ID), messageId, length);
							}

							try {
								//调用扩展消息处理器，如809消息上报
								MessageHandler messageHandler = ServerRuntimeContext.getBean(
										Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(messageId).concat("_ext")
												.concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX),
										MessageHandler.class);
								messageHandler.handle(message);
							} catch (NoSuchBeanDefinitionException e) {

							} catch (Exception e) {
								logger.error(" handler message error,message={}", message, e);
							}
						} else {
							// 非法消息
							Map<String, Object> messageBody = (Map<String, Object>) map
									.get(Constants.MAP_KEY_MESSAGE_BODY);
							messageBody.put(Constants.MAP_KEY_MESSAGE_ID, messageId);
							messageBody.put("desc", "无设备信息");
							Map<String, Object> messageHeader1 = (Map<String, Object>) map
									.get(Constants.MAP_KEY_MESSAGE_HEADER);
							messageHeader1.put(Constants.MAP_KEY_MESSAGE_ID, "unknown");
							messageBatchSaveProducer
									.produce(new BatchMessage(Constants.BATCH_MESSAGE_DEVICE_UP_MESSAGE, message));
						}
					} catch (Exception e) {
						logger.error(" handler message error,message={}", message, e);
					}
				}
			});
		} else if (getSuccessor() != null) {
			getSuccessor().handle(message);
		}
	}

	@SuppressWarnings("unchecked")
	private void updateDataCount(String deviceId, String messageId, int length) {
		String date = DateUtils.format(new Date(), "MMdd");
		String key = deviceId.concat(date);
		Cache cache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_DATA_COUNT_CACHE);
		ValueWrapper value = cache.get(key);
		Map<String, Object> data = null;
		if (value != null) {
			data = (Map<String, Object>) value.get();
		} else {
			data = new HashMap<String, Object>();
		}
		Integer count = (Integer) data.get(messageId);
		if (count == null) {
			count = 1;
		} else {
			count++;
		}
		data.put(messageId, count);
		Integer dataSize = (Integer) data.get("dataSize");
		if (dataSize == null) {
			dataSize = length;
		} else {
			dataSize += length;
		}
		data.put("dataSize", dataSize);
		cache.put(key, data);
	}

}
