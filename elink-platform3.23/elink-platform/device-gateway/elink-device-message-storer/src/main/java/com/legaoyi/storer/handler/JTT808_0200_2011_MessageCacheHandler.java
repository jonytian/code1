package com.legaoyi.storer.handler;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.common.gps.util.GeoHash;
import com.legaoyi.common.gps.util.LngLatConverter;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.storer.util.WebJmsMessage;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011_messageCacheHandler")
public class JTT808_0200_2011_MessageCacheHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0200_2011_MessageCacheHandler.class);

    @Autowired
    @Qualifier("messageBatchSaveProducer")
    private DisruptorEventBatchProducer messageBatchSaveProducer;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Value("${lbs.geohash.cache.enable}")
    private boolean isGeohashCache = false;

    @Value("${device.fault.alarm}")
    private String deviceFaultAlarmFlag;

    private long deviceFaultAlarm;

    @PostConstruct
    public void init() {
        BigInteger bi = new BigInteger(deviceFaultAlarmFlag, 2);
        deviceFaultAlarm = Long.parseLong(bi.toString());
    }

    @Autowired
    public JTT808_0200_2011_MessageCacheHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011_messageAlarmCheckHandler") MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        // 位置信息放缓存
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        boolean isCache = true;
        String lng = (String) messageBody.get("lng");
        String lat = (String) messageBody.get("lat");
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);

        double mileage = 0;
        if (messageBody.get("mileage") != null) {
            mileage = Double.valueOf(String.valueOf(messageBody.get("mileage")));
            Integer initialMileage = (Integer) device.get("initialMileage");
            if (initialMileage != null) {
                // 里程纠正
                messageBody.put("mileage", mileage + initialMileage);
            }
        }

        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
        if (value != null) {
            Map<?, ?> lastGps = (Map<?, ?>) value.get();
            // 这里可以做些位置信息处理的业务判断，比如是否合法的位置信息（时间，漂移等）
            if (lastGps != null && !lastGps.isEmpty()) {
                Long currentGpsTime = (Long) messageBody.get("gpsTime");
                Long lastGpsTime = (Long) lastGps.get("gpsTime");

                // 国外经纬度可认为出现了漂移现象，取上一正常的经纬度
                if (LngLatConverter.outOfChina(Double.parseDouble(lng), Double.parseDouble(lat))) {
                    lng = String.valueOf(lastGps.get("lng"));
                    lat = String.valueOf(lastGps.get("lat"));
                    messageBody.put("lng", lng);
                    messageBody.put("lat", lat);
                }

                if (lastGpsTime > currentGpsTime) {
                    logger.warn("*********currentGps={},lastGps={}", messageBody, lastGps);
                    isCache = false;
                } else {
                    long state = Long.parseLong(String.valueOf(messageBody.get("state")));
                    // acc状态
                    long accState = ((state & (1 << 0)) >> 0);
                    if (accState == 0) {
                        // acc关说明车辆没有在运行，定位器一般读取不到油量以及行驶里程，这里把最后一次的信息补充上，方便后续数据处理
                        double oilmass = Double.valueOf(String.valueOf(messageBody.get("oilmass")));
                        double lastOilmass = Double.valueOf(String.valueOf(lastGps.get("oilmass")));
                        if (lastOilmass > 0 && oilmass == 0) {
                            messageBody.put("oilmass", lastOilmass);
                        }

                        double lastMileage = Double.valueOf(String.valueOf(lastGps.get("mileage")));
                        if (lastMileage > 0 && mileage == 0) {
                            messageBody.put("mileage", lastMileage);
                        }
                    } // end if (accState == 0)

                    // 设置历史告警统计缓存
                    setAlarmStatCache(messageBody, lastGps);

                    // 计算历史最高车速
                    setMaxSpeedOverView(messageBody, lastGps);

                    // 平台计算的停车时长
                    checkParking(message, lastGps, device, accState);

                    // 今日车辆行驶概况
                    setTodayOverView(messageBody, lastGps);

                    // 车辆故障检测
                    checkDeviceFault(device, messageBody, lastGps);

                    message.putExtAttribute("lastGps", lastGps);
                } // end if (lastGpsTime >= currentGpsTime)
            } // end if (lastGps != null)
        } // end if (value != null)

        if (isCache) {
            cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).put(deviceId, messageBody);
            // 缓存GeoHash
            if (isGeohashCache) {
                String enterpriseId = (String) device.get(Constants.MAP_KEY_ENTERPRISE_ID);
                long score = GeoHash.toGeoHash(Double.parseDouble(lng), Double.parseDouble(lat)).toLong();
                redisService.zAdd(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_GEOHASH_CACHE.concat(String.valueOf(enterpriseId)), score, deviceId);
            }

            message.putExtAttribute("isCache", isCache);
            messageBody.remove("yesterdayOverView");
            messageBody.remove("todayOverView");
            messageBody.remove("maxSpeedOverView");
            messageBody.remove("deviceFault");
            messageBody.remove("parkingInfo");
        }
        this.getSuccessor().handle(message);
    }

    @SuppressWarnings("unchecked")
    private void setAlarmStatCache(Map<String, Object> gps, Map<?, ?> lastGps) {
        if (lastGps != null) {
            Map<String, Object> todayAlarmStat = (Map<String, Object>) lastGps.get("todayAlarmStat");
            if (todayAlarmStat != null && !todayAlarmStat.isEmpty()) {
                gps.put("todayAlarmStat", todayAlarmStat);
            }

            Map<String, Object> yesterdayAlarmStat = (Map<String, Object>) lastGps.get("yesterdayAlarmStat");
            if (yesterdayAlarmStat != null && !yesterdayAlarmStat.isEmpty()) {
                gps.put("yesterdayAlarmStat", yesterdayAlarmStat);
            }
        }
    }

    private void setDeviceBizState(Map<?, ?> device, int state) {
        Map<String, Object> data = new HashMap<String, Object>();
        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        data.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
        data.put("state", state);
        messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_DEVICE_BIZ_STATE, data));

        cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_BIZ_STATE_CACHE).put(deviceId, state);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        map.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
        map.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
        if (carId != null) {
            map.put(Constants.MAP_KEY_CAR_ID, carId);
            map.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
        }
        map.put("bizState", state);
        // 消息推送到web页面
        try {
            platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.BIZSTATE_MESSAGE_TYPE, map).toString());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void checkParking(ExchangeMessage message, Map<?, ?> lastGps, Map<?, ?> device, long accState) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> gps = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

        Map<?, ?> enterpriseConfig = (Map<?, ?>) message.getExtAttribute("enterpriseConfig");
        Integer parkingTime = (Integer) enterpriseConfig.get("enterpriseConfig");

        Map<String, Object> parkingInfo = null;
        if (parkingTime != null && parkingTime > 0) {
            Object o = lastGps.get("state");
            long lastState;
            if (o instanceof Long) {
                lastState = (Long) o;
            } else {
                lastState = Long.parseLong(String.valueOf(o));
            }

            parkingInfo = (Map<String, Object>) lastGps.get("parkingInfo");
            if (parkingInfo == null) {
                parkingInfo = new HashMap<String, Object>();
            }

            Long currentGpsTime = (Long) gps.get("gpsTime");
            Long lastGpsTime = (Long) lastGps.get("gpsTime");

            boolean acc = (accState == 1) && (((lastState & (1 << 0)) >> 0) == 1);
            double speed = Double.valueOf(String.valueOf(gps.get("speed")));
            double lastSpeed = Double.valueOf(String.valueOf(lastGps.get("speed")));
            boolean initParking = false;
            if (acc && lastSpeed == 0) {
                // 计算停车时长，定义为行驶过程中的临时停车，即acc开并且车辆停止不动到达设定的时间。
                Long parkingTotalTime = (Long) parkingInfo.get("parkingTotalTime");
                if (parkingTotalTime == null) {
                    if (speed == 0) {
                        // 为可疑的停车点，先存储
                        parkingInfo.put("parkingTime", lastGpsTime);
                        parkingInfo.put("parkingTotalTime", (currentGpsTime - lastGpsTime));
                    }
                } else {
                    boolean saveParkingLog = false;
                    if (speed > 0) {
                        if (parkingTotalTime > parkingTime * 60 * 1000) {
                            saveParkingLog = true;
                            setDeviceBizState(device, 1);
                        }
                        initParking = true;
                    } else {
                        parkingTotalTime += currentGpsTime - lastGpsTime;
                        parkingInfo.put("parkingTotalTime", parkingTotalTime);
                        if (parkingInfo.get("parkingFlag") == null && parkingTotalTime > parkingTime * 60 * 1000) {
                            // 为停车点，设置业务状态为2(acc开，并且超过defaultParkingTime无位移数据)；业务状态,1:行驶中；2:停车；3:熄火
                            parkingInfo.put("parkingFlag", 1);
                            setDeviceBizState(device, 2);
                        }
                    } // end if (speed > 0)

                    if (saveParkingLog) {
                        // 为停车点，存库
                        Map<String, Object> data = new HashMap<String, Object>();
                        data.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
                        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
                        if (carId != null) {
                            data.put(Constants.MAP_KEY_CAR_ID, carId);
                        }
                        data.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
                        data.put("_id", IdGenerator.nextIdStr());
                        data.put("gpsId", lastGps.get("id"));
                        data.put("total", parkingTotalTime);
                        data.put("parkingTime", parkingInfo.get("parkingTime"));
                        data.put("creatTime", System.currentTimeMillis());
                        data.put("lng", gps.get("lng"));
                        data.put("lat", gps.get("lat"));
                        messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_PARKING_LOG, data));

                        gps.put("parkingTime", parkingTotalTime);

                    } // end if (saveParkingLog)
                } // end if (parkingTotalTime == null)
            } else {
                initParking = true;
            } // end if (acc && lastSpeed == 0)

            if (initParking) {
                parkingInfo.remove("parkingTime");
                parkingInfo.remove("parkingFlag");
                parkingInfo.remove("parkingTotalTime");
            }
        } // if (parkingTime != null && parkingTime > 0)

        if (parkingInfo != null) {
            gps.put("parkingInfo", parkingInfo);
        }
    }

    @SuppressWarnings("unchecked")
    private void setTodayOverView(Map<String, Object> gps, Map<?, ?> lastGps) {
        double totalMileage = 0d;
        long drivingTime = 0l;
        double totalOilmass = 0d;
        Long parkingTime = 0l;

        Long gpsTime = (Long) gps.get("gpsTime");
        Map<String, Object> todayOverView = (Map<String, Object>) lastGps.get("todayOverView");
        if (todayOverView == null) {
            todayOverView = new HashMap<String, Object>();
        } else {
            Long lastGpsTime = (Long) lastGps.get("gpsTime");
            if (DateUtils.getDate(gpsTime) > DateUtils.getDate(lastGpsTime)) {
                // 昨日
                Map<String, Object> yesterdayOverView = new HashMap<String, Object>();
                yesterdayOverView.putAll(todayOverView);
                gps.put("yesterdayOverView", yesterdayOverView);
                todayOverView = new HashMap<String, Object>();
            } else {
                totalMileage = (Double) todayOverView.get("totalMileage");
                drivingTime = (Long) todayOverView.get("drivingTime");
                totalOilmass = (Double) todayOverView.get("totalOilmass");
                parkingTime = (Long) todayOverView.get("parkingTime");
            }

            Object o = lastGps.get("oilmass");
            if (o != null) {
                double lastOilmass = Double.valueOf(String.valueOf(o));
                double oilmass = Double.valueOf(String.valueOf(gps.get("oilmass")));
                if (lastOilmass > oilmass) {
                    totalOilmass += (lastOilmass - oilmass);
                }
            }

            o = lastGps.get("mileage");
            if (o != null) {
                double lastMileage = Double.valueOf(String.valueOf(o));
                double mileage = Double.valueOf(String.valueOf(gps.get("mileage")));
                if (mileage > lastMileage) {
                    totalMileage += (mileage - lastMileage);
                }
            }

            long state = Long.parseLong(String.valueOf(lastGps.get("state")));
            // acc状态
            long lastAccState = ((state & (1 << 0)) >> 0);

            state = Long.parseLong(String.valueOf(gps.get("state")));
            // acc状态
            long accState = ((state & (1 << 0)) >> 0);
            if (lastAccState == 1 && accState == 1 && (gpsTime - lastGpsTime) < 10 * 60 * 1000) {
                drivingTime += (gpsTime - lastGpsTime);
            }

            o = gps.get("parkingTime");
            if (o != null) {
                parkingTime += (Long) o;
            }
            gps.remove("parkingTime");
        }
        todayOverView.put("time", gpsTime);
        todayOverView.put("totalMileage", totalMileage);
        todayOverView.put("drivingTime", drivingTime);
        todayOverView.put("parkingTime", parkingTime == null ? 0l : parkingTime);
        todayOverView.put("totalOilmass", totalOilmass);
        todayOverView.put("avgOilmass", totalMileage > 0 && totalOilmass > 0 ? String.format("%.2f", (totalOilmass * 100 / totalMileage)) : "0");
        gps.put("todayOverView", todayOverView);
    }

    @SuppressWarnings("unchecked")
    private void setMaxSpeedOverView(Map<String, Object> gps, Map<?, ?> lastGps) {
        String lng = (String) gps.get("lng");
        String lat = (String) gps.get("lat");
        double speed = Double.valueOf(String.valueOf(gps.get("speed")));
        Long gpsTime = (Long) gps.get("gpsTime");
        Long lastGpsTime = (Long) lastGps.get("gpsTime");

        Map<String, Object> maxSpeedOverView = (Map<String, Object>) lastGps.get("maxSpeedOverView");
        if (maxSpeedOverView == null) {
            maxSpeedOverView = new HashMap<String, Object>();
        }

        boolean bool = false;
        Map<String, Object> todayMaxspeed = (Map<String, Object>) maxSpeedOverView.get("today");
        if (todayMaxspeed == null) {
            todayMaxspeed = new HashMap<String, Object>();
            bool = true;
        } else {
            if (DateUtils.getDate(gpsTime) > DateUtils.getDate(lastGpsTime)) {
                Map<String, Object> yesterdayMaxspeed = new HashMap<String, Object>();
                yesterdayMaxspeed.putAll(todayMaxspeed);
                maxSpeedOverView.put("yesterday", yesterdayMaxspeed);
                bool = true;
            } else {
                double lastSpeed = (Double) todayMaxspeed.get("speed");
                if (speed > lastSpeed) {
                    bool = true;
                }
            }
        }

        if (bool) {
            todayMaxspeed.put("speed", speed);
            todayMaxspeed.put("time", gpsTime);
            todayMaxspeed.put("lng", lng);
            todayMaxspeed.put("lat", lat);
            maxSpeedOverView.put("today", todayMaxspeed);
        }

        bool = false;
        Map<String, Object> monthMaxspeed = (Map<String, Object>) maxSpeedOverView.get("month");
        if (monthMaxspeed == null) {
            bool = true;
        } else {
            if (DateUtils.getMonth(gpsTime) > DateUtils.getMonth(lastGpsTime)) {
                bool = true;
            } else {
                double lastSpeed = (Double) monthMaxspeed.get("speed");
                if (speed > lastSpeed) {
                    bool = true;
                }
            }
        }
        if (bool) {
            maxSpeedOverView.put("month", todayMaxspeed);
        }

        bool = false;
        Map<String, Object> yearMaxspeed = (Map<String, Object>) maxSpeedOverView.get("year");
        if (yearMaxspeed == null) {
            bool = true;
        } else {
            if (DateUtils.getYear(gpsTime) > DateUtils.getYear(lastGpsTime)) {
                bool = true;
            } else {
                double lastSpeed = (Double) yearMaxspeed.get("speed");
                if (speed > lastSpeed) {
                    bool = true;
                }
            }
        }

        if (bool) {
            maxSpeedOverView.put("year", todayMaxspeed);
        }
        gps.put("maxSpeedOverView", maxSpeedOverView);
    }

    @SuppressWarnings("unchecked")
    private void checkDeviceFault(Map<?, ?> device, Map<String, Object> gps, Map<?, ?> lastGps) {
        long alarm = Long.parseLong(String.valueOf(gps.get("alarm")));
        long faultAlarm = 0;
        if (alarm > 0) {
            faultAlarm = alarm & deviceFaultAlarm;
        }

        Map<String, Object> deviceFault = (Map<String, Object>) lastGps.get("deviceFault");
        // 故障已经消除
        if (faultAlarm == 0 && deviceFault != null && deviceFault.get("alarm") != null) {
            deviceFault.remove("alarm");
            saveDeviceFault(device, deviceFault);
        }

        if (faultAlarm > 0) {
            long lastFaultAlarm = 0;
            if (deviceFault == null) {
                deviceFault = new HashMap<String, Object>();
            } else {
                Object o = deviceFault.get("alarm");
                if (o != null) {
                    lastFaultAlarm = (Long) o;
                }
            }
            if (faultAlarm != lastFaultAlarm) {
                deviceFault.put("alarm", faultAlarm);
                // 更新车辆故障
                saveDeviceFault(device, deviceFault);
            }
        }

        if (deviceFault != null && !deviceFault.isEmpty()) {
            gps.put("deviceFault", deviceFault);
        }
    }

    private void saveDeviceFault(Map<?, ?> device, Map<String, Object> deviceFault) {
        // 更新车辆故障
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("_id", device.get(Constants.MAP_KEY_DEVICE_ID));
        map.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        map.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
        if (deviceFault.isEmpty()) {
            map.put("flag", 0);
        } else {
            map.put("flag", 1);
            map.putAll(deviceFault);
        }

        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
        if (carId != null) {
            map.put(Constants.MAP_KEY_CAR_ID, carId);
        }
        map.put("lastUpdateTime", System.currentTimeMillis());
        messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_TYPE_DEVICE_FAULT, map));
    }
}
