package com.legaoyi.data.analyst.task;

import java.util.ArrayList;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.data.mongo.MongoDao;
import com.legaoyi.data.service.CarReportService;
import com.legaoyi.data.service.DeviceService;

@Lazy(false)
@EnableScheduling
@Component("alarmDayReportTask")
public class AlarmDayReportTask {

    private static final Logger logger = LoggerFactory.getLogger(AlarmDayReportTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CarReportService carReportService;

    @Scheduled(cron = "0 5 0 * * ?")
    public void run() {
        Date yesterday = DateUtils.addDays(new Date(), -1);
        try {
            long startTime = yesterday.getTime();
            long endTime = startTime + 24 * 60 * 60 * 1000;
            executeTask(startTime, endTime);
        } catch (Exception e) {
            logger.error("AlarmDayReportTask error", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void executeTask(long startTime, long endTime) throws Exception {
        logger.info("***********AlarmDayReportTask start,startTime={},endTime={}********", startTime, endTime);
        long start = System.currentTimeMillis();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String date = df.format(new Date(startTime));
        String collectionName = "alarm_info";
        int pageSize = 1000;
        int pageNo = 0;
        List<?> list = deviceService.getDevices(startTime, pageSize, pageNo);
        if (list == null || list.isEmpty()) {
            return;
        }

        String fields[] = new String[84];
        fields[83] = "deviceId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);

        int index = 0;
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[index++] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        // 高级驾驶辅助报警
        for (int i = 1; i < 12; i++) {
            String alarm = "adas" + i;
            fields[index++] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        // 驾驶状态监测系统报警信息数据格式
        for (int i = 1; i < 12; i++) {
            String alarm = "dsm" + i;
            fields[index++] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        // 胎压监测系统报警
        for (int i = 0; i < 8; i++) {
            String alarm = "tpm" + i;
            fields[index++] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        // 盲区监测系统报警
        for (int i = 1; i < 4; i++) {
            String alarm = "bsd" + i;
            fields[index++] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        String alarmReportCollectionName = "alarm_day_report_".concat(date.substring(0, 6));
        List<Object> reportList = new ArrayList<Object>();
        while (true) {
            for (Object o : list) {
                Map<String, Object> device = (Map<String, Object>) o;
                String deviceId = (String) device.get("deviceId");
                String carId = (String) device.get("carId");
                String enterpriseId = (String) device.get("enterpriseId");
                
                try {
                    Criteria criteria =  Criteria.where(fields[fields.length - 1]).is(device.get(fields[fields.length - 1]));
                    criteria = criteria.andOperator(Criteria.where("alarmTime").gte(startTime).lt(endTime));
                    
                    Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation,Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
                    AggregationResults<?> result = mongoDao.getMongoTemplate().aggregate(agg, collectionName, Map.class);
                    List<?> results = result.getMappedResults();
                    if (results == null || results.isEmpty()) {
                        continue;
                    }

                    for (Object o1 : results) {
                        Map<?, ?> m = (Map<?, ?>) o1;
                        Map<String, Object> data = new HashMap<String, Object>();
                        boolean isAlarm = false;
                        for (int i = 0; i < (fields.length - 1); i++) {
                            int total = (Integer) m.get(fields[i]);
                            if (total > 0) {
                                data.put(fields[i], total);
                                isAlarm = true;
                            }
                        }

                        if (isAlarm) {
                            // 车辆总报表
                            Map<String, Object> report = new HashMap<String, Object>();
                            report.putAll(data);
                            carReportService.incCarReport(date, device, report);

                            data.put("_id", date.concat("_").concat(deviceId));
                            data.put("deviceId", deviceId);
                            if (carId != null) {
                                data.put("carId", carId);
                            }
                            data.put("enterpriseId", enterpriseId);
                            data.put("date", Integer.parseInt(date));
                            reportList.add(data);
                        }
                    }
                    if (reportList.size() >= 100) {
                        mongoDao.batchSave(reportList, alarmReportCollectionName);
                        reportList = new ArrayList<Object>();
                    }
                } catch (Exception e) {
                    logger.error("AlarmDayReportTask error", e);
                }
            }
            if (list.size() < pageSize) {
                break;
            }
            pageNo++;
            list = deviceService.getDevices(startTime, pageSize, pageNo);
            if (list == null || list.isEmpty()) {
                break;
            }
        }
        if (!reportList.isEmpty()) {
            mongoDao.batchSave(reportList, alarmReportCollectionName);
        }
        logger.info("***********AlarmDayReportTask end,time={} milliseconds********", System.currentTimeMillis() - start);
    }

}
