package com.legaoyi.lbs.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.legaoyi.common.gps.util.DouglasPeucker;
import com.legaoyi.common.gps.util.LngLat;
import com.legaoyi.common.gps.util.ShapeUtil;
import com.legaoyi.common.util.Constants;
import com.legaoyi.lbs.service.GpsInfoService;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.persistence.mongo.dao.MongoDao;
import com.legaoyi.persistence.mongo.service.MongoService;
import com.legaoyi.platform.model.Device;

/**
 * @author gaoshengbo
 */

@Service("gpsInfoService")
@CacheConfig(cacheNames = "gpsInfo")
public class GpsInfoServiceImpl implements GpsInfoService {

    public static final String GPS_INFO_COLLECTION_NAME_PREFIX = "gps_info_";

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao<Map> mongoDao;

    @Autowired
    @Qualifier("mongoService")
    private MongoService mongoService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Value("${douglas.peucker.tolerance}")
    private double tolerance = 1000;

    @Cacheable(value = Constants.CACHE_NAME_DEVICE_GPS_CACHE, key = "#deviceId", unless = "#result == null")
    @Override
    public Map<?, ?> getLastGps(String deviceId) throws Exception {
        Device device = (Device) this.service.get(Device.ENTITY_NAME, deviceId);
        if (device == null || device.getLastOnlineTime() == null) {
            return null;
        }
        // 从Mongodb获取位置信息
        Query query = new Query();
        query.addCriteria(new Criteria("deviceId").is(deviceId));
        query.with(Sort.by(Direction.DESC, "gpsTime"));
        return this.mongoDao.findOne(query, Map.class,
                GPS_INFO_COLLECTION_NAME_PREFIX.concat(new SimpleDateFormat("yyyyMMdd").format(new Date(device.getLastOnlineTime()))));
    }

    // @SuppressWarnings("unchecked")
    // @Override
    // public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo,
    // boolean countable, Map<String, Object> form)
    // throws Exception {
    // // 检查时间范围todo
    // String collectionName =
    // GPS_INFO_COLLECTION_NAME_PREFIX.concat(String.valueOf(form.get("recordDate")).replaceAll("-",
    // ""));
    // /**
    // * 查询条件 格式：{ "conditions":{ "simCode.eq":"013200000000" }, "rangeConditions":[ {
    // * "fieldName":"alarmTime", "from":"2016-12-15 15:25:40", "includeLower":true,
    // * "includeUpper":true, "to":"2016-12-17 15:25:40" } ] }
    // */
    // String enterpriseIdConditionKey = "enterpriseId".concat(".eq");
    // if (form.get(enterpriseIdConditionKey) != null) {
    // Map<String, Object> conditions = (Map<String, Object>) form.get("conditions");
    // if (conditions == null) {
    // conditions = new HashMap<String, Object>();
    // form.put("conditions", conditions);
    // }
    // conditions.put(enterpriseIdConditionKey, form.get(enterpriseIdConditionKey));
    // }
    // if (countable) {
    // return this.mongoService.pageFind(collectionName, selects, orderBy, desc, pageSize, pageNo,
    // form);
    // }
    // return this.mongoService.find(collectionName, selects, orderBy, desc, pageSize, pageNo,
    // form);
    // }

    @Override
    public Map<String, Object> simplify(Map<String, Object> condition) throws Exception {
        String collectionName = GPS_INFO_COLLECTION_NAME_PREFIX.concat(String.valueOf(condition.get("recordDate")).replaceAll("-", ""));
        String[] selects = {"lng", "lat"};
        int pageSize = 3000;
        int pageNo = 1;
        String orderBy = "gpsTime";
        List<Object> list = new ArrayList<>();
        List<?> temp = this.mongoService.find(collectionName, selects, orderBy, false, pageSize, pageNo, condition);
        list.addAll(temp);
        while (temp.size() == pageSize) {
            pageNo++;
            temp = this.mongoService.find(collectionName, selects, orderBy, false, pageSize, pageNo, condition);
            list.addAll(temp);
        }
        List<LngLat> results = new ArrayList<LngLat>();
        for (Object o : list) {
            Map<?, ?> map = (Map<?, ?>) o;
            double lng = Double.parseDouble(String.valueOf(map.get("lng")));
            double lat = Double.parseDouble(String.valueOf(map.get("lat")));
            results.add(new LngLat(lng, lat));
        }
        if (results.isEmpty()) {
            return null;
        }
        List<LngLat> points = DouglasPeucker.simplify(results, this.tolerance);
        double maxDistance = ShapeUtil.getMaxDistance(points.get(0), points);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("points", points);
        map.put("maxDistance", maxDistance);
        return map;
    }

}
