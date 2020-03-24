package com.legaoyi.storer.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.dao.GeneralDao;
import com.legaoyi.storer.service.GeneralService;
import com.legaoyi.storer.util.Constants;

@Transactional
@Service(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200" + Constants.ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX)
public class JTT808_0200_MessageServiceImpl implements GeneralService {

    @Autowired
    @Qualifier("alarmDao")
    private GeneralDao gpsAlarmDao;

    @Autowired
    @Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
    private GeneralDao gpsInfoDao;

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(List<?> list) throws Exception {
        List<Map<String, Object>> gpsInfoList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> gpsAlarmList = new ArrayList<Map<String, Object>>();

        for (Object o : list) {
            ExchangeMessage message = (ExchangeMessage) o;
            Map<?, ?> m = (Map<?, ?>) message.getMessage();
            Map<String, Object> messageBody = (Map<String, Object>) m.get(Constants.MAP_KEY_MESSAGE_BODY);
            Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
            Object enterpriseId = device.get(Constants.MAP_KEY_ENTERPRISE_ID);
            Object deviceId = device.get(Constants.MAP_KEY_DEVICE_ID);
            Object carId = device.get(Constants.MAP_KEY_CAR_ID);
            messageBody.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
            messageBody.put("type", 0);// 普通位置信息
            messageBody.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
            messageBody.put("createTime", message.getCreateTime());
            if (carId != null) {
                messageBody.put(Constants.MAP_KEY_CAR_ID, carId);
            }
            gpsInfoList.add(messageBody);

            // 处理报警
            List<Map<String, Object>> alarmList = (List<Map<String, Object>>) message.getExtAttribute("alarmList");
            if (alarmList != null && !alarmList.isEmpty()) {
                Object gpsId = messageBody.get("id");
                Object alarmTime = messageBody.get("gpsTime");
                for (Map<String, Object> alarmMap : alarmList) {
                    setAlarm(alarmMap, device, gpsId, alarmTime);
                    gpsAlarmList.add(alarmMap);
                }
            }
        }

        if (!gpsAlarmList.isEmpty()) {
            gpsAlarmDao.batchSave(gpsAlarmList);
        }

        gpsInfoDao.batchSave(gpsInfoList);
    }

    private void setAlarm(Map<String, Object> alarmMap, Map<?, ?> device, Object gpsId, Object alarmTime) {
        Object enterpriseId = device.get(Constants.MAP_KEY_ENTERPRISE_ID);
        Object deviceId = device.get(Constants.MAP_KEY_DEVICE_ID);
        Object carId = device.get(Constants.MAP_KEY_CAR_ID);
        alarmMap.put("alarmTime", alarmTime);
        alarmMap.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
        if (carId != null) {
            alarmMap.put(Constants.MAP_KEY_CAR_ID, carId);
        }
        alarmMap.put("gpsId", gpsId);
        alarmMap.put(Constants.MAP_KEY_ENTERPRISE_ID, enterpriseId);
    }
}
