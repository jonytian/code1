package com.legaoyi.data.analyst.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.data.mongo.MongoDao;
import com.legaoyi.data.service.DeviceService;
import com.mongodb.client.result.UpdateResult;

@Lazy(false)
@EnableScheduling
@Component("parkingTimeDayReportTask")
public class ParkingTimeDayReportTask {

    private static final Logger logger = LoggerFactory.getLogger(ParkingTimeDayReportTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private DeviceService deviceService;

    @Scheduled(cron = "0 30 3 * * ?")
    public void run() {
        try {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
            Date yesterday = DateUtils.addDays(new Date(), -1);
            executeTask(df.format(yesterday));
        } catch (Exception e) {
            logger.error("ParkingTimeDayReportTask error", e);
        }
    }

    @SuppressWarnings("rawtypes")
    public void executeTask(String date) throws Exception {
        logger.info("***********ParkingTimeDayReportTask start,date={}********", date);
        long startTime = System.currentTimeMillis();
        String collectionName = "device_parking_logs_".concat(date);
        List<?> list = deviceService.getEnterprises();
        if (list == null || list.isEmpty()) {
            return;
        }

        String month = date.substring(0, 6);
        String parkingTimeReportCollectionName = "parking_time_day_report_".concat(month);
        for (Object o : list) {
            String enterpriseId = (String) o;
            try {
                GroupOperation groupOperation = Aggregation.group("deviceId");
                groupOperation = groupOperation.sum("total").as("total");
                Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where("enterpriseId").is(enterpriseId)), groupOperation, Aggregation.project("total").and("deviceId").previousOperation());

                AggregationResults<Map> result = mongoDao.getMongoTemplate().aggregate(agg, collectionName, Map.class);
                if (result == null || result.getMappedResults().isEmpty()) {
                    continue;
                }

                List<Map> results = result.getMappedResults();
                for (Map map : results) {
                    Long total = Long.parseLong(String.valueOf(map.get("total")));

                    Map<String, Object> data = new HashMap<String, Object>();
                    String deviceId = (String) map.get("deviceId");
                    String id = month.concat("_").concat(deviceId);
                    Query updateQuery = new Query();
                    // 按月每个设备一条统计信息
                    updateQuery.addCriteria(Criteria.where("_id").is(id));
                    Update update = new Update();
                    update.inc("total", total);
                    update.set("d".concat(date.substring(6)), total);
                    UpdateResult updateResult = mongoDao.getMongoTemplate().updateFirst(updateQuery, update, parkingTimeReportCollectionName);
                    if (updateResult.getMatchedCount() == 0) {
                        data.put("total", total);
                        data.put("d".concat(date.substring(6)), total);
                        data.put("_id", id);
                        data.put("deviceId", deviceId);
                        data.put("enterpriseId", enterpriseId);
                        String carId = (String) map.get("carId");
                        if (carId != null) {
                            data.put("carId", carId);
                        }
                        mongoDao.getMongoTemplate().insert(data, parkingTimeReportCollectionName);
                    }
                }
            } catch (Exception e) {
                logger.error("ParkingTimeDayReportTask error", e);
            }
        }
        logger.info("***********ParkingTimeDayReportTask end,time={} milliseconds********", System.currentTimeMillis() - startTime);
    }
}
