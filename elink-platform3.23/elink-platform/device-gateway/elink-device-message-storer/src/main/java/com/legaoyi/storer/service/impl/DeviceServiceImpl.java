package com.legaoyi.storer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.storer.dao.DeviceDao;
import com.legaoyi.common.util.Constants;
import com.legaoyi.storer.service.DeviceService;

@Service("deviceService")
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    @Qualifier("deviceDao")
    private DeviceDao deviceDao;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    public void saveDeviceOnOfflineLogs(List<?> list) throws Exception {
        deviceDao.saveDeviceOnOfflineLogs(list);
    }

    @Override
    public void saveDeviceAccStateLogs(List<?> list) throws Exception {
        deviceDao.saveDeviceAccStateLogs(list);
    }

    public void saveDeviceParkingLogs(List<?> list) throws Exception {
        deviceDao.saveDeviceParkingLogs(list);
    }

    @Override
    public void setDeviceStateOffline(String gatewayId) throws Exception {
        deviceDao.setDeviceStateOffline(gatewayId);
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_DEVICE_INFO_CACHE, key = "#simCode", unless = "#result == null")
    public Map<String, Object> getDeviceInfo(String simCode) throws Exception {
        return deviceDao.getDeviceInfo(simCode);
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE, key = "#deviceId", unless = "#result == null")
    public List<?> getAlarmSetting(String deviceId) throws Exception {
        return deviceDao.getAlarmSetting(deviceId);
    }

    @Override
    @CacheEvict(value = {Constants.CACHE_NAME_DEVICE_INFO_CACHE, Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE}, key = "#deviceId")
    public void resetAlarmSetting(String deviceId) throws Exception {
        deviceDao.resetAlarmSetting(deviceId);
    }

    @Override
    @CacheEvict(value = {Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE}, key = "#deviceId")
    public void resetAlarmSetting(String deviceId, String id, long endTime) throws Exception {
        deviceDao.resetAlarmSetting(id, endTime);
    }

    @Override
    public void batchSetDeviceStateOffline(List<?> list) throws Exception {
        deviceDao.batchSetDeviceStateOffline(list);
    }

    @Override
    public void batchSetDeviceStateOnline(List<?> list) throws Exception {
        deviceDao.batchSetDeviceStateOnline(list);
    }

    public void setDeviceStateUnregistered(String simCode) throws Exception {
        deviceDao.setDeviceStateUnregistered(simCode);
        cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(simCode);
    }

    @Override
    public void batchSetDeviceStateRegistered(List<?> list) throws Exception {
        deviceDao.batchSetDeviceStateRegistered(list);
        for (Object o : list) {
            Map<?, ?> map = (Map<?, ?>) o;
            cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict((String) map.get("simCode"));
        }
    }

    public void batchSetDeviceBizState(final List<?> list) throws Exception {
        deviceDao.batchSetDeviceBizState(list);
    }

}
