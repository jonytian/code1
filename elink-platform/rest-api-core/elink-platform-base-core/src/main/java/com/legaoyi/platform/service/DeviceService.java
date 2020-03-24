package com.legaoyi.platform.service;

import java.util.List;
import java.util.Map;

import com.legaoyi.platform.model.Device;

/**
 * @author gaoshengbo
 */
public interface DeviceService {

    public int getStatus(String id, String gatewayId) throws Exception;
    
    public int getBizState(String id) throws Exception;

    /***
     * 今天曾经上线过的设备
     * 
     * @param enterpriseId
     * @return
     * @throws Exception
     */
    public int staticTodayOnline(String enterpriseId) throws Exception;

    public String getGateway(String id) throws Exception;

    public void save(Device device) throws Exception;

    public List<?> queyUnbindDevice(String enterpriseId, String groupId, int pageSize, int pageNo) throws Exception;

    public void bindDevice(String carId, String deviceId) throws Exception;

    public void unbindDevice(String carId) throws Exception;

    public Map<?, ?> getDeviceCar(String deviceId) throws Exception;

}
