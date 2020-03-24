package com.legaoyi.storer.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.mq.MQMessageProducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.storer.util.WebJmsMessage;

@Component("deviceStateMessageHandler")
public class DeviceStateMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(DeviceStateMessageHandler.class);

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Autowired
    @Qualifier("messageBatchSaveProducer")
    private DisruptorEventBatchProducer messageBatchSaveProducer;

    @Autowired
    @Qualifier("onlineNotifyHandler")
    private OnlineNotifyHandler onlineNotifyHandler;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_LINK_STATUS_MESSAGE)) {
            Map<String, Object> map = (Map<String, Object>) message.getMessage();
            String simCode = (String) map.get(Constants.MAP_KEY_SIM_CODE);
            Map<String, Object> device = deviceService.getDeviceInfo(simCode);
            String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
            Object carId = device.get(Constants.MAP_KEY_CAR_ID);

            int state = (Integer) map.get("state");// 1：上线；0：下线
            // 数据库定义设备状态,0:未注册；1:已注册；2:离线；3:在线;4:已注销；5：已停用
            state = state + 2;
            String gatewayId = message.getGatewayId();
            // 更新缓存，是否需要加分布式锁?暂时不需要
            Map<String, Object> stateMap = new HashMap<String, Object>();
            stateMap.put("state", state);
            stateMap.put("time", message.getCreateTime());
            long lastTime = -1;
            int lastState = -1;

            ValueWrapper cache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).get(deviceId);
            if (cache != null && cache.get() != null) {
                Map<String, Object> lastStateMap = (Map<String, Object>) cache.get();
                lastTime = (Long) lastStateMap.get("time");
                lastState = (Integer) lastStateMap.get("state");
            }

            boolean isNeedLog = true;
            boolean isNeedCache = true;
            switch (state) {
                case 2:
                    // 如果是下线，并且下线时间比上一次上线时间早，则丢弃
                    if (lastState == 3 && message.getCreateTime() < lastTime) {
                        logger.error("下线时间比最后上线时间早，lastTime={},now={}", lastTime, message.getCreateTime());
                        isNeedCache = false;
                    } else if (lastState != 3) {
                        isNeedLog = false;
                    }
                    break;
                case 3:
                    // 如果是上线，并且上线时间比上一次下线时间早，则丢弃
                    if (lastState == 2 && message.getCreateTime() < lastTime) {
                        logger.error("上线时间比最后下线时间早，lastTime={},now={}", lastTime, message.getCreateTime());
                        isNeedCache = false;
                    } else if (lastState == 3) {
                        isNeedLog = false;
                    }
                    break;
            }

            String enterpriseId = (String) device.get(Constants.MAP_KEY_ENTERPRISE_ID);
            if (isNeedCache) {
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).put(deviceId, stateMap);
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE).put(deviceId, gatewayId);
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(deviceId);

                int bizState = (state - 2);

                if (state == 3) {//上线检查上一次acc状态，如果是0，则为熄火
                    ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
                    if (value != null) {
                        Map<?, ?> lastGps = (Map<?, ?>) value.get();
                        if (lastGps != null && !lastGps.isEmpty()) {
                            long accState = Long.parseLong(String.valueOf(lastGps.get("state")));
                            // acc状态
                            if (0 == ((accState & (1 << 0)) >> 0)) {
                                bizState = 3;// 熄火
                            }
                        }
                    }
                }

                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_BIZ_STATE_CACHE).put(deviceId, bizState);

                ValueWrapper staticCache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_ONLINE_TIME_STATIC_CACHE).get(deviceId);

                Map<String, Object> staticMap = null;
                Map<String, Object> todayStat = null;
                if (staticCache != null && staticCache.get() != null) {
                    staticMap = (Map<String, Object>) staticCache.get();
                    todayStat = (Map<String, Object>) staticMap.get("todayStat");
                }

                if (staticMap == null) {
                    staticMap = new HashMap<String, Object>();
                }

                // 统计在线时长
                if (todayStat == null) {
                    todayStat = new HashMap<String, Object>();
                    todayStat.put("total", 0L);
                    todayStat.put("time", message.getCreateTime());
                }

                Date now = new Date(message.getCreateTime());
                long today = DateUtils.formatDate(now).getTime();
                if (state == 2) {
                    // 下线，判断是否跨天
                    long total = (Long) todayStat.get("total");
                    if (DateUtils.getDate(now) - DateUtils.getDate(new Date(lastTime)) > 0) {
                        Map<String, Object> yesterdayStat = new HashMap<String, Object>();
                        yesterdayStat.put("total", (total + today - lastTime));
                        yesterdayStat.put("time", today);
                        staticMap.put("yesterdayStat", yesterdayStat);

                        todayStat.put("total", (message.getCreateTime() - today));
                    } else {
                        todayStat.put("total", total + (message.getCreateTime() - lastTime));
                    }
                    // 保存统计数据，todo
                } else {
                    long time = (Long) todayStat.get("time");
                    if (time < today) {
                        todayStat.put("total", 0L);
                    }
                }

                todayStat.put("time", message.getCreateTime());
                staticMap.put("todayStat", todayStat);
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_ONLINE_TIME_STATIC_CACHE).put(deviceId, staticMap);

                Map<String, Object> updateInfo = new HashMap<String, Object>();
                updateInfo.put("gatewayId", gatewayId);
                updateInfo.put("simCode", simCode);
                updateInfo.put("createTime", message.getCreateTime());
                if (state == 2) {
                    updateInfo.put("type", Constants.DEVICE_STATE_BATCH_SAVE_TYPE_OFFLINE);
                    redisService.decr(gatewayId);
                } else {
                    updateInfo.put("bizState", bizState);
                    updateInfo.put("type", Constants.DEVICE_STATE_BATCH_SAVE_TYPE_ONLINE);
                    redisService.incr(gatewayId);
                }

                messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_DEVICE_STATE, updateInfo));

                map.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
                map.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
                map.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
                if (carId != null) {
                    map.put(Constants.MAP_KEY_CAR_ID, carId);
                    map.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
                }
                // 上下线消息推送到web页面
                platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.ONLINE_MESSAGE_TYPE, map).toString());
            }

            // 记录上下线日志，网关重启时有可能导致终端出现连续两条上线记录而中间没有下线记录
            if (isNeedLog) {
                Map<String, Object> logs = new HashMap<String, Object>();
                logs.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
                logs.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
                if (carId != null) {
                    logs.put(Constants.MAP_KEY_CAR_ID, carId);
                }
                logs.put("state", (state - 2));
                logs.put("createTime", message.getCreateTime());
                logs.put("_id", IdGenerator.nextIdStr());
                messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_ON_OFF_LINE_LOG, logs));
            }

            // 后续处理
            if (state == 3) {
                message.putExtAttribute(Constants.MAP_KEY_DEVICE, device);
                onlineNotifyHandler.handle(message);
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
