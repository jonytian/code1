package com.legaoyi.storer.handler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.legaoyi.storer.service.AlarmService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.storer.util.WebJmsMessage;
import com.legaoyi.common.gps.util.LngLat;
import com.legaoyi.common.gps.util.PolygonUtil;
import com.legaoyi.common.gps.util.ShapeUtil;
import com.legaoyi.common.gps.util.VincentyGeodesy;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.mq.MQMessageProducer;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011_messageAlarmCheckHandler")
public class JTT808_0200_2011_MessageAlarmCheckHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0200_2011_MessageAlarmCheckHandler.class);

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("alarmService")
    private AlarmService alarmService;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Value("${device.continuity.alarm}")
    private String deviceContinuityAlarmFlag;

    private long deviceContinuityAlarm;

    @Value("${device.fault.alarm}")
    private String deviceFaultAlarmFlag;

    private long deviceFaultAlarm;

    @Value("${driving.behavior.alarm}")
    private String drivingBehaviorAlarmFlag;

    private long drivingBehaviorAlarm;

    @Value("${device.urgent.alarm}")
    private String deviceUrgentAlarmFlag;

    private long deviceUrgentAlarm;

    @Autowired
    public JTT808_0200_2011_MessageAlarmCheckHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011_messageAccStateHandler") MessageHandler handler) {
        setSuccessor(handler);
    }

    @PostConstruct
    public void init() {
        BigInteger bi = new BigInteger(deviceContinuityAlarmFlag, 2);
        deviceContinuityAlarm = Long.parseLong(bi.toString());

        bi = new BigInteger(deviceFaultAlarmFlag, 2);
        deviceFaultAlarm = Long.parseLong(bi.toString());

        bi = new BigInteger(drivingBehaviorAlarmFlag, 2);
        drivingBehaviorAlarm = Long.parseLong(bi.toString());

        bi = new BigInteger(deviceUrgentAlarmFlag, 2);
        deviceUrgentAlarm = Long.parseLong(bi.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        Map<?, ?> lastGps = (Map<?, ?>) message.getExtAttribute("lastGps");

        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        boolean alarmSettingEnabled = ((Integer) device.get("alarmSettingEnabled")) == 1 ? true : false;

        List<Map<String, Object>> alarmList = new ArrayList<Map<String, Object>>();
        // 平台报警检查
        if (alarmSettingEnabled) {
            Map<String, Object> alarmMap = checkPlatformAlarm(deviceId, messageBody, lastGps);
            if (alarmMap != null) {
                alarmList.add(alarmMap);
            }
        }

        Map<?, ?> enterpriseConfig = (Map<?, ?>) message.getExtAttribute("enterpriseConfig");

        if (lastGps != null) {
            // 油量异常检测
            Map<String, Object> alarmMap = checkOilmassAlarm(messageBody, lastGps, enterpriseConfig);
            if (alarmMap != null) {
                alarmList.add(alarmMap);
            }

            // 三急行为检测
            alarmMap = checkDangerousDriving(deviceId, messageBody, lastGps, enterpriseConfig);
            if (alarmMap != null) {
                alarmList.add(alarmMap);
            }
        }

        List<Map<String, Object>> list = checkAlarm(deviceId, messageBody, lastGps, enterpriseConfig);
        if (!list.isEmpty()) {
            alarmList.addAll(list);
        }

        Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);

        // 其他协议告警
        List<Map<String, Object>> otherAlarmList = (List<Map<String, Object>>) message.getExtAttribute("alarmList");
        if (otherAlarmList != null) {
            alarmList.addAll(otherAlarmList);
        }
        message.putExtAttribute("alarmList", alarmList);

        // 告警消息推送到web页面
        if (!alarmList.isEmpty()) {
            // 告警统计
            setAlarmStatCache(deviceId, messageBody);

            publishAlarm(alarmList, simCode, device, WebJmsMessage.ALARM_MESSAGE_TYPE);
        }

        this.getSuccessor().handle(message);
    }

    private Map<String, Object> setAlarm(Map<String, Object> gps, long alarm, int type) {
        Map<String, Object> alarmMap = new HashMap<String, Object>();
        int index = 0;
        while (alarm > 0) {
            if (((alarm & (1 << 0)) >> 0) == 1) {
                String alarmKey = "a" + index;
                alarmMap.put(alarmKey, 1);
                setAlarmStat(gps, alarmKey);
            }
            index++;
            alarm = alarm >> 1;
        }

        if (!alarmMap.isEmpty()) {
            // 设备故障类告警
            alarmMap.put("type", type);
            return alarmMap;
        }
        return alarmMap;
    }

    @SuppressWarnings({"unchecked"})
    private List<Map<String, Object>> checkAlarm(String deviceId, Map<String, Object> gps, Map<?, ?> lastGps, Map<?, ?> enterpriseConfig) {
        long alarm = Long.parseLong(String.valueOf(gps.get("alarm")));
        Integer alarmEventId = (Integer) gps.get("alarmEventId");

        long lastAlarm = 0;
        if (lastGps != null) {
            Object o = lastGps.get("alarm");
            if (o instanceof Long) {
                lastAlarm = (Long) o;
            } else {
                lastAlarm = Long.parseLong(String.valueOf(o));
            }
        }

        Long availableAlarm = null;
        Object o = enterpriseConfig.get("availableAlarm");
        if (o != null) {
            availableAlarm = Long.parseLong(String.valueOf(o));
            // 平台允许的告警设置
            alarm = alarm & availableAlarm;
            lastAlarm = lastAlarm & availableAlarm;
        }

        // 是否关键告警，此类告警需要客户确认
        long keyAlarm = 0;
        Map<?, ?> keyAlarmMap = (Map<?, ?>) enterpriseConfig.get("keyAlarm");
        if (keyAlarmMap != null && keyAlarmMap.get("alarm") != null) {
            keyAlarm = Long.parseLong(String.valueOf(keyAlarmMap.get("alarm")));
        }

        List<Map<String, Object>> alarmlist = new ArrayList<Map<String, Object>>();
        if (alarm > 0) {
            long dfAlarm = alarm & deviceFaultAlarm;
            long bdAlarm = alarm & drivingBehaviorAlarm;
            long duAlarm = alarm & deviceUrgentAlarm;

            lastAlarm = ~(lastAlarm & deviceContinuityAlarm);

            if (dfAlarm > 0) {
                String alarmId = null;
                alarm = dfAlarm & lastAlarm;
                if (alarm > 0) {
                    alarmId = IdGenerator.nextIdStr();
                    Map<String, Object> alarmMap = setAlarm(gps, alarm, Constants.AlarmType.DEVICE_FAULT.getType());
                    alarmMap.put("id", alarmId);
                    if (alarmEventId != null) {
                        alarmMap.put("eid", alarmEventId);
                    }
                    if ((alarm & keyAlarm) > 0) {
                        alarmMap.put("k", 1);
                    }
                    alarmlist.add(alarmMap);
                }
                checkContinuityAlarm(deviceId, dfAlarm, alarmId, availableAlarm, gps, lastGps);
            }

            if (bdAlarm > 0) {
                String alarmId = null;
                alarm = bdAlarm & lastAlarm;
                if (alarm > 0) {
                    alarmId = IdGenerator.nextIdStr();
                    Map<String, Object> alarmMap = setAlarm(gps, bdAlarm, Constants.AlarmType.DRIVING_BEHAVIOR.getType());
                    alarmMap.put("id", alarmId);
                    if (alarmEventId != null) {
                        alarmMap.put("eid", alarmEventId);
                    }
                    if ((alarm & keyAlarm) > 0) {
                        alarmMap.put("k", 1);
                    }
                    alarmlist.add(alarmMap);
                }
                checkContinuityAlarm(deviceId, bdAlarm, alarmId, availableAlarm, gps, lastGps);
            }

            if (duAlarm > 0) {
                String alarmId = null;
                alarm = duAlarm & lastAlarm;
                if (alarm > 0) {
                    alarmId = IdGenerator.nextIdStr();
                    Map<String, Object> alarmMap = setAlarm(gps, duAlarm, Constants.AlarmType.DEVICE_URGENT.getType());
                    alarmMap.put("id", alarmId);
                    if (alarmEventId != null) {
                        alarmMap.put("eid", alarmEventId);
                    }
                    if ((alarm & keyAlarm) > 0) {
                        alarmMap.put("k", 1);
                    }
                    alarmlist.add(alarmMap);
                }
                checkContinuityAlarm(deviceId, duAlarm, alarmId, availableAlarm, gps, lastGps);
            }
        } else {
            if (lastAlarm > 0) {
                // 更新连续性性告警最后一次时间,如果上次存在连续性告警，本次不存在说明已经结束，则更新时间
                Map<String, String> lastFistContinuityAlarmMap = (Map<String, String>) lastGps.get("fcAlarm");
                if (lastFistContinuityAlarmMap != null && !lastFistContinuityAlarmMap.isEmpty()) {
                    if (availableAlarm != null) {
                        lastAlarm = lastAlarm & availableAlarm;
                    }
                    // 获得上次连续性的告警
                    long lastContinuityAlarm = lastAlarm & deviceContinuityAlarm;
                    // 更新结束告警时间
                    updateAlarm(alarm, lastContinuityAlarm, lastFistContinuityAlarmMap, lastGps);
                }
            }
        }

        Map<String, String> fcAlarm = (Map<String, String>) gps.get("fcAlarm");
        Map<String, String> lastFistContinuityAlarmMap = null;
        if (lastGps != null) {
            lastFistContinuityAlarmMap = (Map<String, String>) lastGps.get("fcAlarm");
        }

        if (fcAlarm == null) {
            fcAlarm = lastFistContinuityAlarmMap;
        } else {
            if (lastFistContinuityAlarmMap != null && !lastFistContinuityAlarmMap.isEmpty()) {
                fcAlarm.putAll(lastFistContinuityAlarmMap);
            }
        }

        if (fcAlarm != null && !fcAlarm.isEmpty()) {
            // 更新连续性告警缓存
            ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
            if (value != null) {
                Map<String, Object> cache = (Map<String, Object>) value.get();
                cache.put("fcAlarm", fcAlarm);
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).put(deviceId, cache);
            }
        }

        return alarmlist;
    }

    @SuppressWarnings("unchecked")
    private void checkContinuityAlarm(String deviceId, long alarm, String alarmId, Long availableAlarm, Map<String, Object> gps, Map<?, ?> lastGps) {
        long fistContinuityAlarm = 0;
        long lastContinuityAlarm = 0;
        boolean check = false;
        if (lastGps != null) {
            long lastAlarm = Long.parseLong(String.valueOf(lastGps.get("alarm")));
            if (lastAlarm > 0) {
                if (availableAlarm != null) {
                    lastAlarm = lastAlarm & availableAlarm;
                }
                // 获得上次连续性的告警
                lastContinuityAlarm = lastAlarm & deviceContinuityAlarm;
                // 获得第一次出现的连续性告警
                fistContinuityAlarm = ((alarm & deviceContinuityAlarm) | lastContinuityAlarm) & (~lastContinuityAlarm);
                check = true;
            }
        }

        Map<String, String> lastFistContinuityAlarmMap = null;
        if (lastGps != null) {
            lastFistContinuityAlarmMap = (Map<String, String>) lastGps.get("fcAlarm");
            // 更新连续性性告警最后一次时间,如果上次存在连续性告警，本次不存在说明已经结束，则更新时间
            if (lastContinuityAlarm > 0 && lastFistContinuityAlarmMap != null) {
                updateAlarm(alarm, lastContinuityAlarm, lastFistContinuityAlarmMap, lastGps);
            }
        }

        if (!check) {
            fistContinuityAlarm = alarm & deviceContinuityAlarm;
        }

        if (fistContinuityAlarm <= 0 || alarmId == null) {
            return;
        }

        Map<String, String> fistContinuityAlarmMap = (Map<String, String>) gps.get("fcAlarm");
        if (fistContinuityAlarmMap == null) {
            fistContinuityAlarmMap = new HashMap<String, String>();
        }

        // 放入缓存中，
        Long alarmTime = (Long) gps.get("gpsTime");
        gps.put("alarmId", alarmId);
        int index = 0;
        while (alarm > 0) {
            if (((alarm & (1 << 0)) >> 0) == 1) {
                fistContinuityAlarmMap.put("a" + index, alarmId + "_" + alarmTime);
            }
            index++;
            alarm = alarm >> 1;
        }
        if (!fistContinuityAlarmMap.isEmpty()) {
            gps.put("fcAlarm", fistContinuityAlarmMap);
        }
    }

    private void updateAlarm(long alarm, long lastContinuityAlarm, Map<String, String> fistContinuityAlarmMap, Map<?, ?> lastGps) {
        long updateAlarm = lastContinuityAlarm & (~(alarm & deviceContinuityAlarm));
        if (updateAlarm <= 0) {
            return;
        }
        int index = 0;
        Map<String, Map<String, Long>> updateAlarmMap = new HashMap<String, Map<String, Long>>();
        Long lastGpsTime = (Long) lastGps.get("gpsTime");
        while (updateAlarm > 0) {
            if (((updateAlarm & (1 << 0)) >> 0) == 1) {
                String key = "a" + index;
                String val = fistContinuityAlarmMap.get(key);
                if (val != null) {
                    String[] arr = val.split("_");
                    String alarmId = arr[0];
                    Map<String, Long> data = updateAlarmMap.get(alarmId);
                    if (data == null) {
                        data = new HashMap<String, Long>();
                        updateAlarmMap.put(alarmId, data);
                    }
                    data.put("et_" + key, lastGpsTime);
                    fistContinuityAlarmMap.remove(key);
                }
            }
            index++;
            updateAlarm = updateAlarm >> 1;
        }
        if (!updateAlarmMap.isEmpty()) {
            // 更新告警结束时间
            try {
                alarmService.batchUpdate(updateAlarmMap);
            } catch (Exception e) {
                logger.error("******update alarm error", e);
            }
        }
    }

    private void publishAlarm(List<?> alarmList, String simCode, Map<?, ?> device, short type) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("alarmList", alarmList);
        data.put(Constants.MAP_KEY_SIM_CODE, simCode);
        data.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
        data.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
        if (carId != null) {
            data.put(Constants.MAP_KEY_CAR_ID, carId);
        }
        data.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
        data.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        try {
            platformNotifyProducer.send(new WebJmsMessage(type, data).toString());
        } catch (Exception e) {
            logger.error("*******publish alarm error", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> checkDangerousDriving(String deviceId, Map<String, Object> gps, Map<?, ?> lastGps, Map<?, ?> enterpriseConfig) {
        boolean resetTurningAlarmCache = false;

        double lastSpeed = Double.parseDouble(String.valueOf(lastGps.get("speed")));
        double speed = Double.parseDouble(String.valueOf(gps.get("speed")));

        Long lastGpsTime = (Long) lastGps.get("gpsTime");
        Long gpsTime = (Long) gps.get("gpsTime");

        // acc状态
        long state = Long.parseLong(String.valueOf(gps.get("state")));
        long accState = ((state & (1 << 0)) >> 0);

        long lastState;
        Object o = lastGps.get("state");
        if (o instanceof Long) {
            lastState = (Long) o;
        } else {
            lastState = Long.parseLong(String.valueOf(o));
        }
        boolean acc = (accState == 1) && (((lastState & (1 << 0)) >> 0) == 1);

        Map<String, Object> alarmMap = new HashMap<String, Object>();
        // 急加速检测
        Integer accelerationAlarmSetting = (Integer) enterpriseConfig.get("acceleration");
        if (acc && accelerationAlarmSetting != null && accelerationAlarmSetting > 0 && (speed - lastSpeed) * 1000 / (gpsTime - lastGpsTime) >= accelerationAlarmSetting) {
            // carAlarmInfoMap.put(40, "急加速");40=31+9
            Map<String, Object> alarm = new HashMap<String, Object>();
            alarm.put("lastGpsId", lastGps.get("id"));
            alarm.put("currentGpsId", gps.get("id"));
            String alarmKey = "a40";
            alarmMap.put(alarmKey, alarm);
            setAlarmStat(gps, alarmKey);
        }

        // 急刹车检测
        Integer decelerationAlarmSetting = (Integer) enterpriseConfig.get("deceleration");
        if (acc && decelerationAlarmSetting != null && decelerationAlarmSetting > 0 && (lastSpeed - speed) * 1000 / (gpsTime - lastGpsTime) >= decelerationAlarmSetting) {
            // carAlarmInfoMap.put(41, "急刹车");41=31+10
            Map<String, Object> alarm = new HashMap<String, Object>();
            alarm.put("lastGpsId", lastGps.get("id"));
            alarm.put("currentGpsId", gps.get("id"));
            String alarmKey = "a41";
            alarmMap.put(alarmKey, alarm);
            setAlarmStat(gps, alarmKey);
        }

        // 急转弯检测
        Map<String, Object> lastAlarmCacheMap = null;
        Integer turningAlarmSetting = (Integer) enterpriseConfig.get("turning");
        if (turningAlarmSetting != null && turningAlarmSetting > 0) {
            int totalDirection = 0;
            lastAlarmCacheMap = (Map<String, Object>) lastGps.get("dangerousDrivingMap");
            if (lastAlarmCacheMap != null && lastAlarmCacheMap.get("totalDirection") != null) {
                totalDirection = (Integer) lastAlarmCacheMap.get("totalDirection");
            }

            int direction = (Integer) gps.get("direction");
            int lastDirection = (Integer) lastGps.get("direction");
            int d = Math.abs(lastDirection - direction);
            if (acc && speed >= turningAlarmSetting && lastSpeed >= turningAlarmSetting && d > 5 && (gpsTime - lastGpsTime) < 5 * 1000) {// 5秒钟内方向角变化超过5度可能是正在转弯
                totalDirection += d;
                if (totalDirection >= 45) {
                    // carAlarmInfoMap.put(42, "急转弯");
                    Map<String, Object> alarm = new HashMap<String, Object>();
                    alarm.put("lastGpsId", lastGps.get("id"));
                    alarm.put("currentGpsId", gps.get("id"));
                    String alarmKey = "a42";
                    alarmMap.put(alarmKey, alarm);
                    setAlarmStat(gps, alarmKey);
                    totalDirection = 0;
                }
                resetTurningAlarmCache = true;
            } else {
                totalDirection = 0;
                resetTurningAlarmCache = true;
            }

            if (resetTurningAlarmCache) {
                if (lastAlarmCacheMap == null) {
                    lastAlarmCacheMap = new HashMap<String, Object>();
                }
                lastAlarmCacheMap.put("totalDirection", totalDirection);
            }
        } // end 急转弯

        if (alarmMap.isEmpty()) {
            return null;
        }

        if (resetTurningAlarmCache) {
            // 更新缓存
            ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
            if (value != null) {
                Map<String, Object> cache = (Map<String, Object>) value.get();
                cache.put("dangerousDrivingMap", lastAlarmCacheMap);
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).put(deviceId, cache);
            }
        }

        alarmMap.put("id", IdGenerator.nextIdStr());
        alarmMap.put("type", Constants.AlarmType.PLATFORM.getType());// 平台报警
        return alarmMap;
    }

    private Map<String, Object> checkOilmassAlarm(Map<String, Object> gps, Map<?, ?> lastGps, Map<?, ?> enterpriseConfig) {
        double lastOilmass = Double.valueOf(String.valueOf(lastGps.get("oilmass")));
        Long lastGpsTime = (Long) lastGps.get("gpsTime");
        Long gpsTime = (Long) gps.get("gpsTime");
        double oilmass = Double.valueOf(String.valueOf(gps.get("oilmass")));
        long time = (gpsTime - lastGpsTime) / (60 * 1000);

        Integer oilmassAlarmSetting = (Integer) enterpriseConfig.get("oilmass");

        // 油量异常告警处理
        Map<String, Object> alarmMap = new HashMap<String, Object>();
        if (lastOilmass - oilmass < 0) {
            // 加油点
            Map<String, Object> alarm = new HashMap<String, Object>();
            alarm.put("lastGpsId", lastGps.get("id"));
            alarm.put("currentOilmass", oilmass);
            alarm.put("lastOilmass", lastOilmass);
            // carAlarmInfoMap.put(49,"车辆加油提示");
            String alarmKey = "a49";
            alarmMap.put(alarmKey, alarm);
            setAlarmStat(gps, alarmKey);
        } else if (oilmassAlarmSetting != null && (lastOilmass - oilmass) / time > oilmassAlarmSetting) {
            Map<String, Object> alarm = new HashMap<String, Object>();
            alarm.put("lastGpsId", lastGps.get("id"));
            alarm.put("currentOilmass", oilmass);
            alarm.put("lastOilmass", lastOilmass);
            // carAlarmInfoMap.put(25,"车辆油量异常");
            String alarmKey = "a25";
            alarmMap.put(alarmKey, alarm);
            setAlarmStat(gps, alarmKey);
        } // end 油量异常告警处理

        if (alarmMap.isEmpty()) {
            return null;
        }

        alarmMap.put("id", IdGenerator.nextIdStr());
        alarmMap.put("type", Constants.AlarmType.PLATFORM.getType());// 平台报警
        return alarmMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> checkPlatformAlarm(String deviceId, Map<String, Object> gps, Map<?, ?> lastGps) throws Exception {
        double lng = Double.parseDouble(String.valueOf(gps.get("lng")));
        double lat = Double.parseDouble(String.valueOf(gps.get("lat")));
        double lastLat = -1, lastLng = -1;
        if (lastGps != null) {
            lastLat = Double.parseDouble(String.valueOf(lastGps.get("lat")));
            lastLng = Double.parseDouble(String.valueOf(lastGps.get("lng")));
        }
        if (lng == lastLng && lat == lastLat) {
            return null;
        }
        Map<String, Object> alarmMap = new HashMap<String, Object>();
        Map<String, Long> alarmTime = new HashMap<String, Long>();
        List<?> list = deviceService.getAlarmSetting(deviceId);
        list = removeExpiredSetting(deviceId, list);
        if (list != null && !list.isEmpty()) {
            double speed = Double.parseDouble(String.valueOf(gps.get("speed")));
            for (Object o : list) {
                try {
                    Map<?, ?> m = (Map<?, ?>) o;
                    int type = (Integer) m.get("type");

                    // carAlarmInfoMap.put(32,"原地设防报警(平台)");
                    // carAlarmInfoMap.put(33,"出围栏报警(平台)");
                    // carAlarmInfoMap.put(34,"进围栏报警(平台)");
                    // carAlarmInfoMap.put(35,"围栏超速报警(平台)");
                    // carAlarmInfoMap.put(36,"超速报警(平台)");
                    // carAlarmInfoMap.put(37,"路线偏离告警(平台)");
                    // carAlarmInfoMap.put(38,"进入路线告警(平台)");
                    // carAlarmInfoMap.put(39,"路线超速告警(平台)");
                    int at = 31 + type;
                    if (alarmMap.containsKey("a" + at)) {
                        continue;
                    }

                    String settingId = String.valueOf(m.get("id"));
                    Date endTime = (Date) m.get("endTime");
                    Map<?, ?> setting = JsonUtil.convertStringToObject((String) m.get("setting"), Map.class);

                    int alarmType = -1;
                    switch (type) {
                        case 1:
                            alarmType = this.checkMoved(lat, lng, setting, lastGps, endTime, settingId, deviceId);
                            if (alarmType != -1) {
                                alarmTime.put("lastMovedAlarmTime", System.currentTimeMillis());
                            }
                            break;
                        case 2:
                            alarmType = this.checkRail(lat, lng, lastLat, lastLng, speed, setting);
                            if (alarmType != -1) {
                                // 单独标记围栏告警
                                alarmMap.put("r", 1);
                            }
                            break;
                        case 3:
                            alarmType = this.checkOverspeed(speed, setting, lastGps);
                            if (alarmType != -1) {
                                alarmTime.put("lastOverspeedAlarmTime", System.currentTimeMillis());
                                // 单独标记超速告警
                                alarmMap.put("s", 1);
                            }
                            break;
                        case 4:
                            alarmType = this.checkRoute(lat, lng, lastLat, lastLng, speed, setting);
                            if (alarmType != -1) {
                                alarmMap.put("r", 1);
                            }
                            break;
                    }

                    if (alarmType != -1) {
                        Map<String, Object> alarm = new HashMap<String, Object>();
                        alarm.put("id", settingId);
                        alarm.put("name", m.get("name"));

                        // 平台报警从32为开始
                        String alarmKey = "a" + at;
                        alarmMap.put(alarmKey, 1);
                        alarmMap.put("desc" + at, alarm);
                        setAlarmStat(gps, alarmKey);
                    }
                } catch (Exception e) {
                    logger.error("check alarm setting error", e);
                }
            } // end for
        } // end if (list != null && !list.isEmpty())

        if (alarmMap.isEmpty()) {
            return null;
        }

        if (!alarmTime.isEmpty()) {
            // 更新缓存
            ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
            if (value != null) {
                Map<String, Object> cache = (Map<String, Object>) value.get();
                cache.putAll(alarmTime);
                cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).put(deviceId, cache);
            }
        }

        alarmMap.put("id", IdGenerator.nextIdStr());
        alarmMap.put("type", Constants.AlarmType.PLATFORM.getType());// 平台报警
        return alarmMap;
    }

    private List<?> removeExpiredSetting(String deviceId, List<?> list) {
        List<Object> retList = new ArrayList<Object>();
        if (list != null && !list.isEmpty()) {
            Date now = new Date();
            List<Object> reCacheList = new ArrayList<Object>();
            boolean reCache = false;
            for (Object o : list) {
                try {
                    Map<?, ?> m = (Map<?, ?>) o;
                    Date endTime = (Date) m.get("endTime");
                    if (now.after(endTime)) {
                        reCache = true;
                        continue;
                    }
                    reCacheList.add(o);
                    Date startTime = (Date) m.get("startTime");
                    if (startTime.after(now)) {
                        continue;
                    }
                    retList.add(o);
                } catch (Exception e) {
                    logger.error("alarm setting error", e);
                }
            }
            if (reCache) {
                if (retList.isEmpty()) {
                    try {
                        deviceService.resetAlarmSetting(deviceId);
                    } catch (Exception e) {
                        logger.error("resetAlarmSetting error", e);
                    }
                } else {
                    cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE).put(deviceId, retList);
                }
            }
        }
        return retList;
    }

    private int checkOverspeed(double speed, Map<?, ?> setting, Map<?, ?> lastGps) throws Exception {
        // 不支持跨天设置
        // 告警规则：{"speed":"60","interval":"30","startHour":"4","endHour":"16"}
        // 检查上一次超速报警时间
        if (lastGps != null) {
            Long lastTime = (Long) lastGps.get("lastOverspeedAlarmTime");
            if (lastTime != null && (System.currentTimeMillis() - lastTime) < 1000 * Long.parseLong(String.valueOf(setting.get("interval")))) {
                return -1;
            }
        }

        boolean bool = checkHour(setting) && speed > Double.parseDouble(String.valueOf(setting.get("speed")));
        return bool ? 5 : -1;
    }

    private boolean checkHour(Map<?, ?> setting) throws Exception {
        Date now = new Date();
        String date = DateUtils.format(now, DateUtils.DATE_FORMAT);
        String startHour = String.valueOf(setting.get("startHour"));

        Date startTime = DateUtils.parse(date + " " + startHour + ":00", "yyyy-MM-dd HH:ss");
        if (startTime.after(now)) {
            return false;
        }

        String endHour = String.valueOf(setting.get("endHour"));
        Date endTime = DateUtils.parse(date + " " + endHour + ":59:59", DateUtils.DATETIME_FORMAT);
        return endTime.after(now);
    }

    private int checkRail(double lat, double lng, double lastLat, double lastLng, double speed, Map<?, ?> setting) throws Exception {
        if (lastLat == -1 || lastLng == -1) {
            return -1;
        }
        // 不支持跨天设置
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        List<?> weeks = (List<?>) setting.get("week");
        if (!weeks.contains(String.valueOf(week)) || !checkHour(setting)) {
            return -1;
        }
        String inside = String.valueOf(setting.get("inside"));
        String shape = String.valueOf(setting.get("shape"));

        // <option value ="0">出围栏告警</option>
        // <option value ="1">进围栏告警</option>
        // <option value ="2">围栏超速告警</option>
        // carAlarmInfoMap.put(33,"出围栏报警(平台)");
        // carAlarmInfoMap.put(34,"进围栏报警(平台)");
        // carAlarmInfoMap.put(35,"围栏超速报警(平台)");
        if ("1".equals(shape)) {// 圆形
            // 出围栏的边界时告警
            if ("0".equals(inside) && !isInCircle(lat, lng, setting) && isInCircle(lastLat, lastLng, setting)) {
                return 2;
            } else
            // 进围栏的边界时告警
            if ("1".equals(inside) && isInCircle(lat, lng, setting) && !isInCircle(lastLat, lastLng, setting)) {
                return 3;
            } else
            // 区域内超速时告警
            if ("2".equals(inside) && speed > Double.valueOf(String.valueOf(setting.get("speed"))) && isInCircle(lat, lng, setting)) {
                return 4;
            }
        } else if ("4".equals(shape)) {
            // 行政区域,注意由于行政区域的边界点非常多，计算是否在区域内非常耗cpu，将导致性能下降10倍以上，请慎用
            // 出围栏的边界时告警
            if ("0".equals(inside) && !isInDistrict(lat, lng, setting) && isInDistrict(lastLat, lastLng, setting)) {
                return 2;
            } else
            // 进围栏的边界时告警
            if ("1".equals(inside) && isInDistrict(lat, lng, setting) && !isInDistrict(lastLat, lastLng, setting)) {
                return 3;
            } else
            // 区域内超速时告警
            if ("2".equals(inside) && speed > Double.valueOf(String.valueOf(setting.get("speed"))) && isInDistrict(lat, lng, setting)) {
                return 4;
            }
        } else {// 多边形
            String path = (String) setting.get("path");
            // 出围栏的边界时告警
            if ("0".equals(inside) && !isInPolygon(lat, lng, path) && isInPolygon(lastLat, lastLng, path)) {
                return 2;
            } else
            // 进围栏的边界时告警
            if ("1".equals(inside) && isInPolygon(lat, lng, path) && !isInPolygon(lastLat, lastLng, path)) {
                return 3;
            } else
            // 区域内超速时告警
            if ("2".equals(inside) && speed > Double.valueOf(String.valueOf(setting.get("speed"))) && isInPolygon(lat, lng, path)) {
                return 4;
            }
        }
        return -1;
    }

    private int checkRoute(double lat, double lng, double lastLat, double lastLng, double speed, Map<?, ?> setting) throws Exception {
        // 不支持跨天设置
        if (lastLat == -1 || lastLng == -1) {
            return -1;
        }

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        List<?> weeks = (List<?>) setting.get("week");
        if (!weeks.contains(String.valueOf(week)) || !checkHour(setting)) {
            return -1;
        }
        // carAlarmInfoMap.put(37,"路线偏离告警(平台)");
        // carAlarmInfoMap.put(38,"进入路线告警(平台)");
        // carAlarmInfoMap.put(39,"路线超速告警(平台)");

        String route = (String) setting.get("route");
        int width = Integer.parseInt(String.valueOf(setting.get("width")));
        double maxDistance = Double.parseDouble(String.valueOf(setting.get("maxDistance")));
        String inside = String.valueOf(setting.get("inside"));
        Map<String, Object> checkInfo = new HashMap<String, Object>();
        if ("0".equals(inside) && isInRoute(lastLat, lastLng, route, width, maxDistance, null) && !isInRoute(lat, lng, route, width, maxDistance, checkInfo)) {
            LngLat a = (LngLat) checkInfo.get("a");
            if (a != null) {
                LngLat b = (LngLat) checkInfo.get("b");
                // 判断是否由于路线行驶结束而导致的路线偏离
                if (isObtuseTriangle(a, b, new LngLat(lng, lat))) {
                    return -1;
                }
            }
            return 6;
        } else if ("1".equals(inside) && isInRoute(lat, lng, route, width, maxDistance, null) && !isInRoute(lastLat, lastLng, route, width, maxDistance, null)) {
            return 7;
        } else if ("2".equals(inside) && speed > Double.parseDouble(String.valueOf(setting.get("speed"))) && isInRoute(lat, lng, route, width, maxDistance, null)) {
            return 8;
        }
        return -1;
    }

    private int checkMoved(double lat, double lng, Map<?, ?> setting, Map<?, ?> lastGps, Date endTime, String settingId, String deviceId) {
        // 规则格式：{"duration":"10","interval":"30","radius":150,"lat":22.526716,"lng":114.034399}
        // 检查上一次原地设防告警时间
        Long lastTime = (Long) lastGps.get("lastMovedAlarmTime");
        Long currentTimeMillis = System.currentTimeMillis();
        if (lastTime != null && (currentTimeMillis - lastTime) < 1000 * Long.parseLong(String.valueOf(setting.get("interval")))) {
            return -1;
        }

        boolean bool = !isInCircle(lat, lng, setting);
        if (bool) {
            long expiredTime = currentTimeMillis + Integer.parseInt(String.valueOf(setting.get("duration"))) * 60 * 1000;
            try {
                if (expiredTime < endTime.getTime()) {
                    deviceService.resetAlarmSetting(deviceId, settingId, expiredTime);
                }
            } catch (Exception e) {
                logger.error("resetAlarmSetting error", e);
            }
        }
        return bool ? 1 : -1;
    }

    private boolean isInCircle(double lat, double lng, Map<?, ?> setting) {
        double centerlng = Double.valueOf(String.valueOf(setting.get("lng")));
        double centerlat = Double.valueOf(String.valueOf(setting.get("lat")));
        double radius = Double.parseDouble(String.valueOf(setting.get("radius")));
        return ShapeUtil.isInCircle(lng, lat, centerlng, centerlat, radius);
    }

    private boolean isInPolygon(double lat, double lng, String lngLats) {
        List<LngLat> areas = new ArrayList<LngLat>();
        String arr[] = lngLats.split(";");
        for (String s : arr) {
            String a[] = s.split(",");
            LngLat lngLat = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
            areas.add(lngLat);
        }
        return PolygonUtil.isPointInPolygon(lng, lat, areas);
    }

    private boolean isInRoute(double lat, double lng, String route, int routeWidth, double maxDistance, Map<String, Object> checkInfo) {
        String arr[] = route.split(";");
        if (arr.length > 1) {
            String a[] = arr[0].split(",");
            // 此处可提高性能
            double distance = VincentyGeodesy.distanceInMeters(new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1])), new LngLat(lng, lat));
            if (distance > (routeWidth + maxDistance)) {
                return false;
            }

            int i = 1;
            for (; i < arr.length; i++) {
                String a1[] = arr[i].split(",");
                double height = ShapeUtil.pointToLine(Double.valueOf(a[1]), Double.valueOf(a[0]), Double.valueOf(a1[1]), Double.valueOf(a1[0]), lat, lng);
                if (routeWidth >= height) {
                    return true;
                }
                a = a1;
            }
            // 计算路线偏离时用来计算是否路线行驶结束，而不是真的偏离
            if (checkInfo != null && i == arr.length) {
                LngLat last = new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1]));
                if (distance > VincentyGeodesy.distanceInMeters(last, new LngLat(lng, lat))) {
                    // 车是从路线起点驶向终点
                    a = arr[arr.length - 2].split(",");
                    checkInfo.put("a", new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1])));
                    checkInfo.put("b", last);
                } else {
                    // 车是重路线终点驶向起点
                    a = arr[1].split(",");
                    checkInfo.put("a", new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1])));
                    a = arr[0].split(",");
                    checkInfo.put("b", new LngLat(Double.valueOf(a[0]), Double.valueOf(a[1])));
                }
            }
        }
        return false;
    }

    private boolean isInDistrict(double lat, double lng, Map<?, ?> setting) {
        Object bounds = setting.get("bounds");
        if (bounds == null || !(bounds instanceof List)) {
            return false;
        }
        List<?> list = (List<?>) bounds;
        if (list == null || list.isEmpty()) {
            return false;
        }

        // 此处可以提高性能
        Map<?, ?> center = (Map<?, ?>) setting.get("center");
        if (center != null && !this.isInCircle(lat, lng, center)) {
            return false;
        }

        for (Object bound : list) {
            String path = (String) bound;
            if (isInPolygon(lat, lng, path)) {
                return true;
            }
        }
        return false;
    }

    /***
     * 判断三个经纬度组成的三角形ABC是否是以c边为长边钝角三角形
     * 
     * @param pointA
     * @param pointB
     * @param pointC
     * @return
     */
    private static boolean isObtuseTriangle(LngLat pointA, LngLat pointB, LngLat pointC) {
        double a = VincentyGeodesy.distanceInMeters(pointA, pointB);
        double b = VincentyGeodesy.distanceInMeters(pointB, pointC);
        double c = VincentyGeodesy.distanceInMeters(pointC, pointA);
        double max = Math.max(a, b);
        if (max > c) {
            return false;
        }

        // 设三角形三边分别是a b c （假设c是最长的那条边）
        // 如果a^2 + b^2 = c^2,则为直角
        // 如果a^2 + b^2 < c^2, 则为钝角
        // 如果a^2 + b^2 > c^2, 则为锐角
        if (!(a + b > c && Math.abs(a - b) < c)) {
            return false;// "无法构成三角形";
        } else if (a == b || a == c || b == c) {
            return false;// "等腰三角形";
        } else if (a * a + b * b == c * c) {
            return false;// "直角三角形";
        } else if (a * a + b * b < c * c) {
            return true;// "钝角三角形";
        } else {
            return false;// "锐角三角形";
        }
    }

    @SuppressWarnings("unchecked")
    private void setAlarmStatCache(String deviceId, Map<?, ?> gps) {
        Map<String, Integer> alarmStat = (Map<String, Integer>) gps.get("alarmStat");
        if (alarmStat == null || alarmStat.isEmpty()) {
            return;
        }
        gps.remove("alarmStat");
        // 这里可以加锁，后续待观察是否需要加锁
        ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
        Map<String, Object> cache = null;
        if (value == null || value.get() == null) {
            return;
        }
        cache = (Map<String, Object>) value.get();

        Long lastAlarmTime = null;
        Map<String, Object> todayAlarmStat = (Map<String, Object>) cache.get("todayAlarmStat");
        if (todayAlarmStat != null) {
            lastAlarmTime = (Long) todayAlarmStat.get("time");
        }

        if (todayAlarmStat == null) {
            todayAlarmStat = new HashMap<String, Object>();
        }

        Long gpsTime = (Long) gps.get("gpsTime");

        Map<String, Object> yesterdayAlarmStat = null;
        if (lastAlarmTime != null && !todayAlarmStat.isEmpty() && DateUtils.getDate(gpsTime) > DateUtils.getDate(lastAlarmTime)) {
            // 昨日
            yesterdayAlarmStat = new HashMap<String, Object>();
            yesterdayAlarmStat.putAll(todayAlarmStat);
            todayAlarmStat = new HashMap<String, Object>();
        }

        for (String alarm : alarmStat.keySet()) {
            Integer total = (Integer) todayAlarmStat.get(alarm);
            if (total != null) {
                todayAlarmStat.put(alarm, (total + alarmStat.get(alarm)));
            } else {
                todayAlarmStat.put(alarm, alarmStat.get(alarm));
            }
        }
        todayAlarmStat.put("time", gpsTime);

        if (yesterdayAlarmStat != null) {
            cache.put("yesterdayAlarmStat", yesterdayAlarmStat);
        }
        cache.put("todayAlarmStat", todayAlarmStat);
        cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).put(deviceId, cache);
    }

    @SuppressWarnings("unchecked")
    private void setAlarmStat(Map<String, Object> gps, String alarmKey) {
        Map<String, Integer> alarmStat = (Map<String, Integer>) gps.get("alarmStat");
        if (alarmStat == null) {
            alarmStat = new HashMap<String, Integer>();
        }
        alarmStat.put(alarmKey, 1);
        gps.put("alarmStat", alarmStat);
    }
}
