package com.legaoyi.platform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Constants;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.model.Car;
import com.legaoyi.platform.model.Device;
import com.legaoyi.platform.model.EnterpriseConfig;
import com.legaoyi.platform.service.DeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Cacheable(value = Constants.CACHE_NAME_CAR_INFO_CACHE, key = "#deviceId")
    public Map<?, ?> getDeviceCar(String deviceId) throws Exception {
        String sql = "SELECT id AS carId, enterprise_id AS enterpriseId, plate_number AS plateNumber,plate_color as plateColor FROM v_car_device WHERE device_id = ?";
        List<?> list = this.service.findBySql(sql, deviceId);
        if (list != null && !list.isEmpty()) {
            return (Map<?, ?>) list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int getStatus(String id, String gatewayId) throws Exception {
        if (gatewayId != null) {
            ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_STATE_CACHE.concat(gatewayId)).get(id);
            if (cache != null && cache.get() != null) {
                Map<String, Object> stateMap = (Map<String, Object>) cache.get();
                return (Integer) stateMap.get("state");
            }
        }
        Device device = (Device) this.service.get(Device.ENTITY_NAME, id);
        if (device != null) {
            return device.getState();
        }
        return -1;
    }

    @Override
    public int getBizState(String id) throws Exception {
        ValueWrapper cache = this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_BIZ_STATE_CACHE).get(id);
        if (cache != null && cache.get() != null) {
            Object bizState = (Object) cache.get();
            if (bizState instanceof Integer) {
                return (Integer) bizState;
            } else {
                return Integer.parseInt(String.valueOf(bizState));
            }
        }
        Device device = (Device) this.service.get(Device.ENTITY_NAME, id);
        if (device != null) {
            return device.getBizState();
        }
        return 0;
    }

    public int staticTodayOnline(String enterpriseId) throws Exception {
        long time = DateUtils.parse(DateUtils.format(new Date(), DateUtils.DATE_FORMAT), DateUtils.DATE_FORMAT).getTime();
        String sql = "select count(1) as total from device where (state = 3 or last_offline_time >= ?) and enterprise_id = ? ";
        return (int) service.countBySql(sql, time, enterpriseId);
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_DEVICE_GATEWAY_CACHE, key = "#id", unless = "#result == null")
    public String getGateway(String id) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, id);
        if (device != null) {
            return device.getGatewayId();
        }
        return null;
    }

    @Override
    public void save(Device device) throws Exception {
        String enterpriseId = device.getEnterpriseId();
        EnterpriseConfig config = (EnterpriseConfig) this.service.get(EnterpriseConfig.ENTITY_NAME, enterpriseId);
        if (config != null && config.getDeviceLimit() != null) {
            Integer deviceLimit = config.getDeviceLimit();
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("enterpriseId", enterpriseId);
            long count = this.service.count(Device.ENTITY_NAME, andCondition);
            if (deviceLimit > 0 && count >= deviceLimit) {
                logger.warn("new device limit,enterpriseId={},limit={}", enterpriseId, deviceLimit);
                throw new BizProcessException("该账号允许接入的最大设备数量为" + deviceLimit + ",目前已超出限制");
            }
        }

        long count = this.service.countBySql("select * from device where sim_code=?", device.getSimCode());
        if (count > 0) {
            throw new BizProcessException("sim卡号已经存在");
        }

        this.service.persist(device);

        Car car = new Car();
        car.setEnterpriseId(device.getEnterpriseId());
        car.setDeviceId(device.getId());
        car.setPlateNumber(device.getName());
        car.setCreateUser(device.getCreateUser());
        setCar(car);
        service.persist(car);
    }

    @Override
    public List<?> queyUnbindDevice(String enterpriseId, String groupId, int pageSize, int pageNo) throws Exception {
        String sql = "SELECT id,name as name,enterprise_id as enterpriseId FROM device a where not exists (SELECT id FROM car b WHERE a.id = b.device_id AND b.enterprise_id = ? ) AND enterprise_id = ? ";
        List<Object> params = new ArrayList<Object>();
        params.add(enterpriseId);
        params.add(enterpriseId);
        if (StringUtils.isNoneBlank(groupId)) {
            sql += " AND group_id = ?";
            params.add(groupId);
        }
        return this.service.findBySqlWithPage(sql, pageSize, pageNo, params.toArray());
    }

    @Override
    public void bindDevice(String carId, String deviceId) throws Exception {
        Map<String, Object> andCondition = Maps.newHashMap();
        andCondition.put("deviceId.eq", deviceId);
        if (this.service.count(Car.ENTITY_NAME, andCondition) > 0) {
            throw new BizProcessException("该车载设备已被其他车辆使用 ！");
        }

        Car car = (Car) this.service.get(Car.ENTITY_NAME, carId);
        if (car != null) {
            car.setDeviceId(deviceId);
            this.service.merge(car);
        }
    }

    @Override
    public void unbindDevice(String carId) throws Exception {
        Car car = (Car) this.service.get(Car.ENTITY_NAME, carId);
        if (car != null) {
            this.cacheManager.getCache(Constants.CACHE_NAME_CAR_INFO_CACHE).evict(car.getDeviceId());
            car.setDeviceId(null);
            this.service.merge(car);
        }
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
