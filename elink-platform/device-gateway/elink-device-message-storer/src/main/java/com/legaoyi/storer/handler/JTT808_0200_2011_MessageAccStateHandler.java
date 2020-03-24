package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.storer.util.WebJmsMessage;
import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011_messageAccStateHandler")
public class JTT808_0200_2011_MessageAccStateHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0200_2011_MessageAccStateHandler.class);

    @Autowired
    @Qualifier("messageBatchSaveProducer")
    private DisruptorEventBatchProducer messageBatchSaveProducer;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        Map<String, Object> gpsInfo = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

        long state = Long.parseLong(String.valueOf(gpsInfo.get("state")));
        // acc状态
        long accState = ((state & (1 << 0)) >> 0);

        Long currentGpsTime = (Long) gpsInfo.get("gpsTime");
        Map<?, ?> lastGps = (Map<?, ?>) message.getExtAttribute("lastGps");
        if (lastGps != null) {
            Object o = lastGps.get("state");
            long lastState;
            if (o instanceof Long) {
                lastState = (Long) o;
            } else {
                lastState = Long.parseLong(String.valueOf(o));
            }

            Long lastGpsTime = (Long) lastGps.get("gpsTime");
            if (lastGpsTime > currentGpsTime) {
                return;
            }
            if (accState != ((lastState & (1 << 0)) >> 0)) {
                saveLog((short) accState, gpsInfo, device);
            }
        } else {
            // 处理时时间可能出现滞后
            if (message.getExtAttribute("isCache") != null) {
                saveLog((short) accState, gpsInfo, device);
            }
        }
    }

    /***
     * 车辆状态变化日志
     * 
     * @param state
     * @param gpsInfo
     * @param device
     */

    private void saveLog(short state, Map<String, Object> gpsInfo, Map<?, ?> device) {
        Map<String, Object> logs = new HashMap<String, Object>();
        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        logs.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        logs.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
        if (carId != null) {
            logs.put(Constants.MAP_KEY_CAR_ID, carId);
        }
        logs.put("state", state);
        logs.put("gpsTime", gpsInfo.get("gpsTime"));
        logs.put("lat", gpsInfo.get("lat"));
        logs.put("lng", gpsInfo.get("lng"));
        logs.put("oilmass", gpsInfo.get("oilmass"));
        logs.put("mileage", gpsInfo.get("mileage"));
        logs.put("_id", IdGenerator.nextIdStr());
        messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_ACC_STATE_LOG, logs));

        // 更新设备的业务状态，业务状态,1:行驶中；2:停车；3:熄火
        int bizState = (state == 0 ? 3 : 1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
        map.put("state", bizState);
        messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_DEVICE_BIZ_STATE, map));

        cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_BIZ_STATE_CACHE).put(deviceId, bizState);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        data.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
        data.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
        if (carId != null) {
            data.put(Constants.MAP_KEY_CAR_ID, carId);
            data.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
        }
        data.put("bizState", bizState);
        // 消息推送到web页面
        try {
            platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.BIZSTATE_MESSAGE_TYPE, data).toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
