package com.legaoyi.data.analyst.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.data.mongo.MongoDao;
import com.legaoyi.data.service.CarReportService;
import com.legaoyi.data.service.DeviceService;
import com.mongodb.client.result.UpdateResult;

@Lazy(false)
@EnableScheduling
@Component("accTimeDayReportTask")
public class AccTimeDayReportTask {

    private static final Logger logger = LoggerFactory.getLogger(AccTimeDayReportTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CarReportService carReportService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void run() {
        try {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
            Date yesterday = DateUtils.addDays(new Date(), -1);
            executeTask(df.format(yesterday));
        } catch (Exception e) {
            logger.error("AccTimeDayReportTask error", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void executeTask(String date) throws Exception {
        logger.info("***********AccTimeDayReportTask start,date={}********", date);
        long startTime = System.currentTimeMillis();
        String collectionName = "acc_state_logs_".concat(date);
        int pageSize = 1000;
        int pageNo = 0;

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date currentDate = df.parse(date);
        Date nextDate = DateUtils.addDays(currentDate, 1);
        List<?> list = deviceService.getDevices(currentDate.getTime(), pageSize, pageNo);
        if (list == null || list.isEmpty()) {
            return;
        }

        Document dbObject = new Document();
        Document fieldsObject = new Document();
        fieldsObject.put("gpsTime", true);
        fieldsObject.put("state", true);
        String month = date.substring(0, 6);
        String accTimeReportCollectionName = "acc_time_day_report_".concat(month);
        while (true) {
            for (Object o : list) {
                Map<String, Object> device = (Map<String, Object>) o;
                String deviceId = (String) device.get("deviceId");
                String carId = (String) device.get("carId");
                String enterpriseId = (String) device.get("enterpriseId");
                Long lastOfflineTime = (Long) device.get("lastOfflineTime");
                Long lastOnlineTime = (Long) device.get("lastOnlineTime");
                short state = (Short) device.get("state");

                try {
                    Query query = new BasicQuery(dbObject, fieldsObject);
                    query.with(new Sort(Direction.ASC, "gpsTime"));
                    query.addCriteria(new Criteria("deviceId").is(deviceId));
                    query.addCriteria(new Criteria("gpsTime").gt(currentDate.getTime()));
                    List<?> results = mongoDao.getMongoTemplate().find(query, Map.class, collectionName);

                    long accOnTime = currentDate.getTime();
                    int lastState = -1;
                    long total = 0;
                    if (results != null) {
                        for (Object o1 : results) {
                            Map<String, Object> data = (Map<String, Object>) o1;
                            int logState = (Integer) data.get("state");
                            if (logState == lastState) {
                                continue;
                            }
                            lastState = logState;
                            long gpsTime = (Long) data.get("gpsTime");
                            // acc关日志
                            if (logState == 0) {
                                total += gpsTime - accOnTime;
                            } else if (logState == 1) {
                                accOnTime = gpsTime;
                            }
                        }
                    }

                    // acc开启之后一直没有关，说明一直在开车
                    if (lastState == 1) {
                        if (state == 3 && lastOnlineTime < nextDate.getTime() || lastOfflineTime != null && lastOfflineTime >= nextDate.getTime()) {
                            total += nextDate.getTime() - accOnTime;
                        } else {
                            if (lastOfflineTime != null && lastOfflineTime < nextDate.getTime()) {
                                long time = lastOfflineTime - accOnTime;
                                if (time > 0) {
                                    total += time;
                                }
                            }
                        }
                    }
                    if (total > 24 * 60 * 60 * 1000) {
                        total = 24 * 60 * 60 * 1000;
                    }
                    if (total > 0) {
                        Map<String, Object> data = new HashMap<String, Object>();
                        String id = month.concat("_").concat(deviceId);
                        Query updateQuery = new Query();
                        // 按月每个设备一条统计信息
                        updateQuery.addCriteria(Criteria.where("_id").is(id));
                        Update update = new Update();
                        update.inc("total", total);
                        update.set("d".concat(date.substring(6)), total);
                        UpdateResult result = mongoDao.getMongoTemplate().updateFirst(updateQuery, update, accTimeReportCollectionName);
                        if (result.getMatchedCount() == 0) {
                            data.put("total", total);
                            data.put("d".concat(date.substring(6)), total);
                            data.put("_id", id);
                            data.put("deviceId", deviceId);
                            data.put("enterpriseId", enterpriseId);
                            if (carId != null) {
                                data.put("carId", carId);
                            }
                            mongoDao.getMongoTemplate().insert(data, accTimeReportCollectionName);
                        }

                        // 车辆总报表
                        Map<String, Object> report = new HashMap<String, Object>();
                        report.put("totalDrivingTime", total);
                        carReportService.incCarReport(date, device, report);
                    }
                } catch (Exception e) {
                    logger.error("AccTimeDayReportTask error", e);
                }
            }
            if (list.size() < pageSize) {
                break;
            }
            pageNo++;
            list = deviceService.getDevices(currentDate.getTime(), pageSize, pageNo);
            if (list == null || list.isEmpty()) {
                break;
            }
        }
        logger.info("***********AccTimeDayReportTask end,time={} milliseconds********", System.currentTimeMillis() - startTime);
    }
}
