package com.legaoyi.storer.dao;

import java.util.List;

public interface DeviceDownMessageDao extends GeneralDao {

    public void setMessageState(int state, String id) throws Exception;

    public List<?> getOfflineMessage(String deviceId) throws Exception;

    public void delMessage(String id) throws Exception;
}
