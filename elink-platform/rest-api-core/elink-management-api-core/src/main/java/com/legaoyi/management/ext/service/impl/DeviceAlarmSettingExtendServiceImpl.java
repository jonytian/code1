package com.legaoyi.management.ext.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.legaoyi.common.util.Constants;
import com.legaoyi.management.model.DeviceAlarmSetting;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Device;

/**
 * @author gaoshengbo
 */
@Service("deviceAlarmSettingExtendService")
public class DeviceAlarmSettingExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return DeviceAlarmSetting.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        String deviceIdStr = (String) entity.get("deviceId");
        String[] deviceIds = deviceIdStr.split(",");
        DeviceAlarmSetting setting = null;
        Map<String, Object> map = Maps.newHashMap();
        for (String deviceId : deviceIds) {// 支持批量设置
            entity.put("deviceId", deviceId);
            setting = (DeviceAlarmSetting) super.persist(entity);
            Device device = (Device) this.service.get(Device.ENTITY_NAME, setting.getDeviceId());
            if (device.getAlarmSettingEnabled() != Device.ALARM_SETTING_ENABLED) {
                device.setAlarmSettingEnabled(Device.ALARM_SETTING_ENABLED);
                this.service.merge(device);
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getSimCode());
            }
            this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE).evict(setting.getDeviceId());
            map.put("id", device.getId());
        }
        return map;
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        DeviceAlarmSetting setting = (DeviceAlarmSetting) super.merge(id, entity);
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE).evict(setting.getDeviceId());
        return setting;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            DeviceAlarmSetting setting = (DeviceAlarmSetting) this.service.get(getEntityName(), id);
            this.service.delete(setting);
            this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_ALARM_SETTING_CACHE).evict(setting.getDeviceId());
        }
    }
}
