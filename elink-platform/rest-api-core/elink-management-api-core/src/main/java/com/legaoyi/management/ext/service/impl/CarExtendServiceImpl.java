package com.legaoyi.management.ext.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.persistence.mongo.dao.MongoDao;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Car;
import com.legaoyi.platform.model.Device;

/**
 * @author gaoshengbo
 */
@Service("carExtendService")
public class CarExtendServiceImpl extends DefaultExtendService {

    private static final Logger logger = LoggerFactory.getLogger(CarExtendServiceImpl.class);

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao<Map> mongoDao;

    @Override
    protected String getEntityName() {
        return Car.ENTITY_NAME;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_CAR_INFO_CACHE, key = "#id", unless = "#result == null")
    public Object get(Object id) throws Exception {
        return this.service.get(getEntityName(), id);
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        setEntity(entity);
        Car officersCar = (Car) super.persist(entity);
        evictCache(officersCar.getDeviceId());
        return officersCar;
    }

    @Override
    @CacheEvict(value = Constants.CACHE_NAME_CAR_INFO_CACHE, key = "#id")
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        setEntity(entity);

        String enterpriseId = (String) entity.get("enterpriseId");
        if (StringUtils.isNotEmpty(enterpriseId)) {
            Car oldCar = (Car) this.get(id);
            if (!oldCar.getEnterpriseId().equals(enterpriseId)) {
                // 更新绑定设备的企业
                Device device = (Device) this.service.get(Device.ENTITY_NAME, oldCar.getDeviceId());
                device.setEnterpriseId(enterpriseId);
                this.service.merge(device);
                String month = DateUtils.format(new Date(), "yyyyMM");
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(month.concat("_").concat(device.getId())));
                Update update = new Update();
                update.set("enterpriseId", enterpriseId);
                String[] cols = new String[] {"online_time_day_report_", "gps_info_day_report_", "device_data_count_", "alarm_day_report_", "acc_time_day_report_"};
                for (String col : cols) {
                    mongoDao.getMongoTemplate().updateFirst(query, update, col.concat(month));
                }
                mongoDao.getMongoTemplate().updateFirst(query, update, "car_report");
            }
        }

        Car officersCar = (Car) super.merge(id, entity);
        evictCache(officersCar.getDeviceId());
        return officersCar;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            Car officersCar = (Car) this.service.get(getEntityName(), id);
            if (officersCar == null) {
                continue;
            }
            this.service.delete(officersCar);
            this.cacheManager.getCache(Constants.CACHE_NAME_CAR_INFO_CACHE).evict(officersCar.getId());
            evictCache(officersCar.getDeviceId());
        }
    }

    private void evictCache(String deviceId) throws Exception {
        if (StringUtils.isNotBlank(deviceId)) {
            Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
            if (device != null) {
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getId());
                this.cacheManager.getCache(Constants.CACHE_NAME_DEVICE_INFO_CACHE).evict(device.getSimCode());
                this.cacheManager.getCache(Constants.CACHE_NAME_CAR_INFO_CACHE).evict(device.getId());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setEntity(Map<String, Object> entity) {
        try {
            String plateNumber = (String) entity.get("plateNumber");
            if (StringUtils.isNotBlank(plateNumber)) {
                String sql = "SELECT province_code as provinceCode,city_code as cityCode FROM system_plate_number_rule WHERE prefix = ? limit 1";
                List<?> list = this.service.findBySql(sql, plateNumber.substring(0, 2));
                if (list != null && !list.isEmpty()) {
                    entity.putAll((Map<String, Object>) list.get(0));
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
