package com.legaoyi.storer.dao;

import java.util.Map;

public interface AlarmDao extends GeneralDao {

    public void saveDataLimitAlarm(final Map<String, Object> map);

    public void batchUpdate(final Map<String, Map<String, Long>> data) throws Exception;
}
