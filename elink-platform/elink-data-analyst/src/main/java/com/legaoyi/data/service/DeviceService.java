package com.legaoyi.data.service;

import java.util.List;

public interface DeviceService {

    public List<?> getDevices(long lastOfflineTime, int pageSize, int pageNo) throws Exception;

    public List<?> getEnterprises() throws Exception;

    public void updateLongOffline(int state, long time) throws Exception;

}
