package com.legaoyi.data.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.legaoyi.data.mongo.MongoDao;
import com.legaoyi.data.service.CarReportService;
import com.mongodb.client.result.UpdateResult;

@Service("carReportService")
public class CarReportServiceImpl implements CarReportService {

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Override
    public void updateCarReport(String date, Map<String, Object> device, Map<String, Object> data) {
        String deviceId = (String) device.get("deviceId");
        String carId = (String) device.get("carId");
        String enterpriseId = (String) device.get("enterpriseId");

        String month = date.substring(0, 6);
        String id = month.concat("_").concat(deviceId);

        Query query = new Query();
        // 按月每个设备一条统计信息
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        for (String key : data.keySet()) {
            update.set(key, data.get(key));
        }
        UpdateResult result = mongoDao.getMongoTemplate().updateFirst(query, update, "car_report");
        if (result.getMatchedCount() == 0) {
            data.put("_id", id);
            data.put("month", Integer.parseInt(month));
            data.put("year", Integer.parseInt(date.substring(0, 4)));
            data.put("devieId", deviceId);
            data.put("enterpriseId", enterpriseId);
            if (carId != null) {
                data.put("carId", carId);
            }
            mongoDao.getMongoTemplate().insert(data, "car_report");
        }
    }

    @Override
    public void incCarReport(String date, Map<String, Object> device, Map<String, Object> data) {
        String deviceId = (String) device.get("deviceId");
        String carId = (String) device.get("carId");
        String enterpriseId = (String) device.get("enterpriseId");

        String month = date.substring(0, 6);
        String id = month.concat("_").concat(deviceId);

        Query query = new Query();
        // 按月每个设备一条统计信息
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if (value instanceof Long) {
                update.inc(key, (Long) value);
            } else if (value instanceof Integer) {
                update.inc(key, (Integer) value);
            } else if (value instanceof Double) {
                update.inc(key, (Double) value);
            } else if (value instanceof Float) {
                update.inc(key, (Float) value);
            } else {
                update.inc(key, Integer.parseInt(String.valueOf(value)));
            }
        }
        UpdateResult result = mongoDao.getMongoTemplate().updateFirst(query, update, "car_report");
        if (result.getMatchedCount() == 0) {
            data.put("_id", id);
            data.put("month", Integer.parseInt(month));
            data.put("year", Integer.parseInt(date.substring(0, 4)));
            data.put("devieId", deviceId);
            data.put("enterpriseId", enterpriseId);
            if (carId != null) {
                data.put("carId", carId);
            }
            mongoDao.getMongoTemplate().insert(data, "car_report");
        }
    }

}
