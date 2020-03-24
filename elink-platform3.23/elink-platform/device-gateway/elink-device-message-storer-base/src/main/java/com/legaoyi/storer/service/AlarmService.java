package com.legaoyi.storer.service;

import java.util.Map;

public interface AlarmService {

    public void saveDataLimitAlarm(Map<String, Object> map);
    
    public void batchUpdate(final Map<String, Map<String, Long>> data) throws Exception;
}
