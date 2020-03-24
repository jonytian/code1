package com.legaoyi.storer.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.legaoyi.storer.dao.AlarmDao;
import com.legaoyi.storer.service.AlarmService;

@Service("alarmService")
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    @Qualifier("alarmDao")
    private AlarmDao alarmDao;

    @Override
    public void saveDataLimitAlarm(Map<String, Object> map) {
        alarmDao.saveDataLimitAlarm(map);
    }

    @Override
    public void batchUpdate(Map<String, Map<String, Long>> data) throws Exception {
        alarmDao.batchUpdate(data);
    }

}
