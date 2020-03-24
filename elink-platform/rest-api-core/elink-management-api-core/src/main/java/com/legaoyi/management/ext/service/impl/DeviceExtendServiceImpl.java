package com.legaoyi.management.ext.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Constants;
import com.legaoyi.persistence.jpa.util.Reflect2Entity;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Car;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.model.EnterpriseConfig;

/**
 * @author gaoshengbo
 */
@Service("deviceExtendService")
public class DeviceExtendServiceImpl extends DefaultExtendService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceExtendServiceImpl.class);

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Override
    protected String getEntityName() {
        return Device.ENTITY_NAME;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_DEVICE_INFO_CACHE, key = "#id", unless = "#result == null")
    public Object get(Object id) throws Exception {
        return this.service.get(getEntityName(), id);
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        String enterpriseId = (String) entity.get("enterpriseId");
        EnterpriseConfig config = (EnterpriseConfig) this.service.get(EnterpriseConfig.ENTITY_NAME, enterpriseId);
        if (config != null && config.getDeviceLimit() != null) {
            Integer deviceLimit = config.getDeviceLimit();
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("enterpriseId", enterpriseId);
            long count = this.service.count(getEntityName(), andCondition);
            if (deviceLimit > 0 && count >= deviceLimit) {
                logger.warn("new device limit,enterpriseId={},limit={}", enterpriseId, deviceLimit);
                throw new BizProcessException("该账号允许接入的最大设备数量为" + deviceLimit + ",目前已超出限制");
            }
        }

        String simCode = (String) entity.get("simCode");
        simCode = simCode.toLowerCase();
        Map<String, Object> andCondition = new HashMap<String, Object>();
        andCondition.put("simCode", simCode);
        long count = this.service.count(getEntityName(), andCondition);
        if (count > 0) {
            throw new BizProcessException("设备ID已经存在");
        }
        Device device = (Device) super.persist(entity);

        Object isCreateCar = entity.get("isCreateCar");
        if (!StringUtils.isBlank(device.getName()) && isCreateCar != null && "1".equals(String.valueOf(isCreateCar))) {
            Car car = new Car();
            Reflect2Entity.reflect(car, entity);
            car.setId(null);
            car.setState(Car.CAR_STATE_FREE);
            car.setEnterpriseId(device.getEnterpriseId());
            car.setDeviceId(device.getId());
            car.setPlateNumber(device.getName());
            car.setCreateUser(device.getCreateUser());
            setCar(car);
            service.persist(car);
        }
        return device;
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        String oldSimCode = (String) entity.get("oldSimCode");
        String simCode = (String) entity.get("simCode");
        if (oldSimCode != null && simCode != null && !oldSimCode.equals(simCode.toUpperCase())) {
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("simCode", simCode);
            long count = this.service.count(getEntityName(), andCondition);
            if (count > 0) {
                throw new BizProcessException("设备ID已经存在");
            }
        }

        Device device = (Device) this.service.merge(getEntityName(), id, entity);
        evictCache(device);
        if (device.getState() >= 4) {
            String gatewayId = device.getGatewayId();
            if (gatewayId != null) {
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).evict(device.getId());
            }
            this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE).evict(device.getId());
            cacheManager.getCache(Constants.CACHE_NAME_DEVICE_GPS_CACHE).evict(device.getId());
            this.redisService.zRem(Constants.CACHE_NAME_DEVICE_GPS_GEOHASH_CACHE.concat(device.getEnterpriseId()), device.getId());
        }
        return device;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("deviceId", id);
            long count = this.service.count(Car.ENTITY_NAME, andCondition);
            if (count > 0) {
                throw new BizProcessException("该设备已被车辆绑定，不能删除！");
            }
        }
        for (Object id : ids) {
            Device device = (Device) this.service.get(getEntityName(), id);
            if (device == null) {
                continue;
            }
            this.service.delete(device);
            if (device.getState() > 1) {
                String gatewayId = device.getGatewayId();
                if (gatewayId != null) {
                    this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).evict(device.getId());
                }
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE).evict(device.getId());
                cacheManager.getCache(Constants.CACHE_NAME_DEVICE_GPS_CACHE).evict(device.getId());
                this.redisService.zRem(Constants.CACHE_NAME_DEVICE_GPS_GEOHASH_CACHE.concat(device.getEnterpriseId()), device.getId());
            }
            evictCache(device);
        }
    }

    private void evictCache(Device device) throws Exception {
        // jtt-storer加载了这些缓存，需要清除
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getName());
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getTerminalId());
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getId());
        this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getSimCode());
    }

    private void setCar(Car car) {
        try {
            String plateNumber = car.getPlateNumber();
            if (StringUtils.isNotBlank(plateNumber)) {
                String sql = "SELECT province_code as provinceCode,city_code as cityCode FROM system_plate_number_rule WHERE prefix = ? limit 1";
                List<?> list = this.service.findBySql(sql, plateNumber.substring(0, 2));
                if (list != null && !list.isEmpty()) {
                    Map<?, ?> map = (Map<?, ?>) list.get(0);
                    car.setProvinceCode((String) map.get("provinceCode"));
                    car.setCityCode((String) map.get("cityCode"));
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
