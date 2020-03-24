package com.legaoyi.storer.tjsatl.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.service.AlarmService;
import com.legaoyi.storer.service.ConfigService;
import com.legaoyi.storer.tjsatl.service.FileServerService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_tjsatl" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Tjsatl_0200_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Tjsatl_0200_MessageHandler.class);

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    @Qualifier("fileServerService")
    private FileServerService fileServerService;

    @Autowired
    @Qualifier("alarmService")
    private AlarmService alarmService;

    @Autowired
    @Qualifier("deviceDownMessageSendHandler")
    private MessageHandler deviceDownMessageSendHandler;

    @Autowired
    @Qualifier("configService")
    private ConfigService configService;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    public Tjsatl_0200_MessageHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2016" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        String id = IdGenerator.nextIdStr();
        messageBody.put("id", id);

        String alarmId = IdGenerator.nextIdStr();

        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        Map<?, ?> enterpriseConfig = this.configService.getEnterpriseConfig((String) device.get(Constants.MAP_KEY_ENTERPRISE_ID));
        message.putExtAttribute("enterpriseConfig", enterpriseConfig);
        Map<?, ?> keyAlarmMap = (Map<?, ?>) enterpriseConfig.get("keyAlarm");

        // 告警与位置信息关联
        Map<String, Object> otherAlarm = new HashMap<String, Object>();

        /** 高级驾驶辅助系统报警信息 **/
        Map<String, Object> adasAlarm = (Map<String, Object>) messageBody.get("adasAlarm");
        boolean attachment = false;
        if (adasAlarm != null) {
            Integer flag = (Integer) adasAlarm.get("flag");
            Integer type = (Integer) adasAlarm.get("type");
            String alarmKey = "adas" + type;
            if (flag != null && flag == 2) {
                updateContinuityAlarm(message, alarmKey);
            } else {
                otherAlarm.put(alarmKey, 1);
                otherAlarm.put("adas", type);
                otherAlarm.put("level", adasAlarm.get("level"));

                if (checkKeyAlarm(keyAlarmMap, "adas", type)) {
                    otherAlarm.put("k", 1);
                }
                setAlarmStat(messageBody, alarmKey);

                attachment = checkAttachment(message, adasAlarm, id);
                if (flag != null && flag == 1) {
                    // 事件开始
                    setFistContinuityAlarm(messageBody, alarmKey, alarmId);
                }
            }
        }

        /** 驾驶员状态监测系统报警信息 **/
        Map<String, Object> dsmAlarm = (Map<String, Object>) messageBody.get("dsmAlarm");
        if (dsmAlarm != null) {
            Integer flag = (Integer) dsmAlarm.get("flag");
            Integer type = (Integer) dsmAlarm.get("type");
            String alarmKey = "dsm" + type;
            if (flag != null && flag == 2) {
                updateContinuityAlarm(message, alarmKey);
            } else {
                otherAlarm.put(alarmKey, 1);
                otherAlarm.put("dsm", type);
                otherAlarm.put("level", dsmAlarm.get("level"));

                if (checkKeyAlarm(keyAlarmMap, "dsm", type)) {
                    otherAlarm.put("k", 1);
                }
                setAlarmStat(messageBody, alarmKey);

                attachment = checkAttachment(message, dsmAlarm, id);
                if (flag != null && flag == 1) {
                    // 事件开始
                    setFistContinuityAlarm(messageBody, alarmKey, alarmId);
                }
            }
        }

        /** 胎压监测系统报警信息 **/
        Map<String, Object> tpmAlarm = (Map<String, Object>) messageBody.get("tpmAlarm");
        if (tpmAlarm != null) {
            Integer flag = (Integer) tpmAlarm.get("flag");
            if (flag != null && flag == 2) {
                updateContinuityAlarm(message, "tpm");
            } else {
                if (tpmAlarm.get("alarmList") != null) {
                    List<?> alarmList = (List<?>) tpmAlarm.get("alarmList");
                    for (Object o : alarmList) {
                        Map<?, ?> m = (Map<?, ?>) o;
                        // 11111110=254
                        Integer type = (Integer) m.get("type");
                        if (type != null && (type & 254) > 0) {
                            otherAlarm.put("tpm" + type, 1);
                            otherAlarm.put("seq", m.get("seq"));
                        }
                        if (checkKeyAlarm(keyAlarmMap, "tpm", type)) {
                            otherAlarm.put("k", 1);
                        }
                    }
                    String alarmKey = "tpm";
                    otherAlarm.put(alarmKey, 1);
                    setAlarmStat(messageBody, alarmKey);

                    attachment = checkAttachment(message, tpmAlarm, id);
                    if (flag != null && flag == 1) {
                        // 事件开始
                        setFistContinuityAlarm(messageBody, "tpm", alarmId);
                    }
                }
            }
        }

        /** 盲区监测系统报警信息 **/
        Map<String, Object> bsdAlarm = (Map<String, Object>) messageBody.get("bsdAlarm");
        if (bsdAlarm != null) {
            Integer flag = (Integer) bsdAlarm.get("flag");
            Integer type = (Integer) bsdAlarm.get("type");
            String alarmKey = "bsd" + type;
            if (flag != null && flag == 2) {
                updateContinuityAlarm(message, alarmKey);
            } else {
                otherAlarm.put(alarmKey, 1);
                otherAlarm.put("bsd", type);
                if (checkKeyAlarm(keyAlarmMap, "bsd", type)) {
                    otherAlarm.put("k", 1);
                }
                setAlarmStat(messageBody, alarmKey);

                attachment = checkAttachment(message, bsdAlarm, id);
                if (flag != null && flag == 1) {
                    // 事件开始
                    setFistContinuityAlarm(messageBody, alarmKey, alarmId);
                }
            }
        }

        if (!otherAlarm.isEmpty()) {
            if (attachment) {
                otherAlarm.put("att", 1);
            }
            // 苏标告警类型
            otherAlarm.put("type", Constants.AlarmType.TJSATL.getType());
            otherAlarm.put("id", alarmId);

            List<Map<String, Object>> otherAlarmList = new ArrayList<Map<String, Object>>();
            otherAlarmList.add(otherAlarm);
            message.putExtAttribute("alarmList", otherAlarmList);
        }
        // 后续其他业务处理
        getSuccessor().handle(message);
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
                ExchangeMessage exchangeMessage = new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE, downMessage, null, message.getGatewayId());
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

    @SuppressWarnings("unchecked")
    private void setFistContinuityAlarm(Map<String, Object> gps, String alarmKey, String alarmId) {
        Map<String, String> fistContinuityAlarmMap = (Map<String, String>) gps.get("fcAlarm");
        if (fistContinuityAlarmMap == null) {
            fistContinuityAlarmMap = new HashMap<String, String>();
        }
        Long alarmTime = (Long) gps.get("gpsTime");
        gps.put("alarmId", alarmId);

        fistContinuityAlarmMap.put(alarmKey, alarmId + "_" + alarmTime);
        gps.put("fcAlarm", fistContinuityAlarmMap);
    }

    @SuppressWarnings("unchecked")
    private void updateContinuityAlarm(ExchangeMessage message, String alarmKey) {
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        ValueWrapper value = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
        if (value != null) {
            Map<?, ?> lastGps = (Map<?, ?>) value.get();
            if (lastGps != null && !lastGps.isEmpty()) {
                Map<String, String> fistContinuityAlarmMap = (Map<String, String>) lastGps.get("fcAlarm");
                if (fistContinuityAlarmMap == null) {
                    return;
                }

                Map<?, ?> map = (Map<?, ?>) message.getMessage();
                Map<String, Object> gps = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
                Long lastGpsTime = (Long) lastGps.get("gpsTime");
                Long gpsTime = (Long) gps.get("gpsTime");
                if (lastGpsTime > gpsTime) {
                    return;
                }

                String val = fistContinuityAlarmMap.get(alarmKey);
                if (val != null) {
                    String[] arr = val.split("_");
                    String alarmId = arr[0];
                    Map<String, Long> data = new HashMap<String, Long>();
                    data.put("et_" + alarmKey, lastGpsTime);
                    try {
                        Map<String, Map<String, Long>> updateAlarmMap = new HashMap<String, Map<String, Long>>();
                        updateAlarmMap.put(alarmId, data);
                        alarmService.batchUpdate(updateAlarmMap);
                    } catch (Exception e) {
                        logger.error("******update alarm error", e);
                    }
                    fistContinuityAlarmMap.remove(alarmKey);
                    cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).put(deviceId, lastGps);
                }
            }
        }
    }

    private boolean checkKeyAlarm(Map<?, ?> keyAlarmMap, String key, Integer type) {
        if (keyAlarmMap == null || type == null) {
            return false;
        }
        String keyAlarm = (String) keyAlarmMap.get(key);
        if (keyAlarm != null) {
            String[] arr = keyAlarm.split(",");
            for (String s : arr) {
                if (type == Integer.parseInt(s)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void setAlarmStat(Map<String, Object> gps, String alarmKey) {
        Map<String, Integer> alarmStat = (Map<String, Integer>) gps.get("alarmStat");
        if (alarmStat == null) {
            alarmStat = new HashMap<String, Integer>();
            gps.put("alarmStat", alarmStat);
        }
        alarmStat.put(alarmKey, 1);
    }
}
