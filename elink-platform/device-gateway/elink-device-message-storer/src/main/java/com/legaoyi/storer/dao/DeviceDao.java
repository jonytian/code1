package com.legaoyi.storer.dao;

import java.util.List;
import java.util.Map;

public interface DeviceDao {

    public void batchSetDeviceStateOnline(final List<?> list) throws Exception;

    public void batchSetDeviceStateOffline(final List<?> list) throws Exception;

    public void saveDeviceOnOfflineLogs(List<?> list) throws Exception;

    public void saveDeviceAccStateLogs(List<?> list) throws Exception;
    
    public void saveDeviceParkingLogs(List<?> list) throws Exception;

    public void setDeviceStateOffline(String gatewayId) throws Exception;

    public void setDeviceStateUnregistered(String simCode) throws Exception;

    public void batchSetDeviceStateRegistered(final List<?> list) throws Exception;

    public void batchSetDeviceBizState(final List<?> list) throws Exception;

    public Map<String, Object> getDeviceInfo(String simCode) throws Exception;

    public List<?> getAlarmSetting(String deviceId) throws Exception;

    public void resetAlarmSetting(String deviceId) throws Exception;

    public void resetAlarmSetting(String id, long endTime) throws Exception;

    public Map<String, Object> getEnterpriseConfig(String enterpriseId) throws Exception;
}
