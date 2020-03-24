package com.legaoyi.data.analyst.task;

import java.math.BigDecimal;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;

import com.legaoyi.data.mongo.MongoDao;
import com.legaoyi.data.service.CarReportService;
import com.legaoyi.data.service.DeviceService;

@Lazy(false)
@EnableScheduling
@Component("mileageAndOilmassDayReportTask")
public class MileageAndOilmassDayReportTask {

    private static final Logger logger = LoggerFactory.getLogger(MileageAndOilmassDayReportTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CarReportService carReportService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 5 * * ?")
    public void run() {
        try {
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
            Date yesterday = DateUtils.addDays(new Date(), -1);
            executeTask(df.format(yesterday));
        } catch (Exception e) {
            logger.error("MileageAndOilmassDayReportTask error", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void executeTask(String date) throws Exception {
        logger.info("***********MileageAndOilmassDayReportTask start,date={}********", date);
        long startTime = System.currentTimeMillis();
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        int pageSize = 1000;
        int pageNo = 0;
        Date currentDate = df.parse(date);
        List<?> list = deviceService.getDevices(currentDate.getTime(), pageSize, pageNo);
        if (list == null || list.isEmpty()) {
            return;
        }
        String gpsInfoReportCollectionName = "gps_info_day_report_".concat(date.substring(0, 6));
        // List<Object> gpsInfoReportList = new ArrayList<Object>();

        String gpsCollectionName = "gps_info_".concat(date);
        String alarmCollectionName = "alarm_info_".concat(date);
        Document fieldsObject = new Document();
        fieldsObject.put("mileage", true);
        fieldsObject.put("oilmass", true);

        Document alarmFieldsObject = new Document();
        alarmFieldsObject.put("desc49.currentOilmass", true);
        alarmFieldsObject.put("desc49.lastOilmass", true);

        String month = date.substring(0, 6);
        while (true) {
            for (Object o : list) {
                Map<String, Object> device = (Map<String, Object>) o;
                String deviceId = (String) device.get("deviceId");
                String carId = (String) device.get("carId");
                String enterpriseId = (String) device.get("enterpriseId");
                try {
                    Document dbObject = new Document();
                    Query ascQuery = new BasicQuery(dbObject, fieldsObject);
                    ascQuery.with(new Sort(Direction.ASC, "gpsTime"));
                    ascQuery.addCriteria(new Criteria("deviceId").is(deviceId));
                    ascQuery.addCriteria(new Criteria("gpsTime").gte(currentDate.getTime()));
                    // mileage没有索引，这里会比较慢；后台已对acc关的gps数据进行了处理，这条件不必要了
                    // ascQuery.addCriteria(new Criteria("mileage").gt(0));
                    Map<?, ?> firstMap = mongoDao.getMongoTemplate().findOne(ascQuery, Map.class, gpsCollectionName);
                    if (firstMap == null || firstMap.isEmpty()) {
                        continue;
                    }

                    dbObject = new Document();
                    Query descQuery = new BasicQuery(dbObject, fieldsObject);
                    descQuery.with(new Sort(Direction.DESC, "gpsTime"));
                    descQuery.addCriteria(new Criteria("deviceId").is(deviceId));
                    // mileage没有索引，这里会比较慢；后台已对acc关的gps数据进行了处理，这条件不必要了
                    // descQuery.addCriteria(new Criteria("mileage").gt(0));
                    Map<?, ?> lastMap = mongoDao.getMongoTemplate().findOne(descQuery, Map.class, gpsCollectionName);
                    if (lastMap == null || lastMap.isEmpty()) {
                        continue;
                    }
                    double firstMileage = (Double) firstMap.get("mileage");
                    double lastMileage = (Double) lastMap.get("mileage");

                    double mileage = lastMileage - firstMileage;
                    if (mileage <= 0) {
                        continue;
                    }

                    double firstOilmass = (Double) firstMap.get("oilmass");
                    double lastOilmass = (Double) lastMap.get("oilmass");

                    double oilmass = 0;
                    // 找出加油点
                    dbObject = new Document();
                    Query alarmQuery = new BasicQuery(dbObject, alarmFieldsObject);
                    alarmQuery.addCriteria(new Criteria("deviceId").is(deviceId));
                    alarmQuery.addCriteria(new Criteria("a49").is(1));
                    List<?> alarmList = mongoDao.getMongoTemplate().find(alarmQuery, Map.class, alarmCollectionName);
                    double addOilmass = 0;
                    if (alarmList != null) {
                        for (Object o1 : alarmList) {
                            Map<?, ?> alarm = (Map<?, ?>) o1;
                            Map<?, ?> desc49 = (Map<?, ?>) alarm.get("desc49");
                            if (desc49 != null) {
                                Object currentOilmassObj = desc49.get("currentOilmass");
                                Object lastOilmassObj = desc49.get("lastOilmass");
                                if (currentOilmassObj != null && lastOilmassObj != null) {
                                    addOilmass += (Double) currentOilmassObj - (Double) lastOilmassObj;
                                }
                            }

                        }
                    }

                    double avgOilmass = 0;
                    oilmass = firstOilmass - lastOilmass + addOilmass;
                    if (oilmass > 0) {
                        avgOilmass = oilmass * 100 / mileage;
                        BigDecimal b = new BigDecimal(avgOilmass);
                        avgOilmass = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    } else {
                        oilmass = 0;
                    }

                    String id = month.concat("_").concat(deviceId);
                    double totalMileage = mileage;
                    double totalOilmass = oilmass;
                    Map<String, Object> data = mongoDao.getMongoTemplate().findById(id, Map.class, gpsInfoReportCollectionName);
                    if (data == null) {
                        data = new HashMap<String, Object>();
                        data.put("_id", id);
                        data.put("deviceId", deviceId);
                        if (carId != null) {
                            data.put("carId", carId);
                        }
                        data.put("enterpriseId", enterpriseId);
                    } else {
                        if (data.get("totalMileage") != null) {
                            totalMileage += (Double) data.get("totalMileage");
                        }
                        if (data.get("totalOilmass") != null) {
                            totalOilmass += (Double) data.get("totalOilmass");
                        }
                    }
                    
                    double totalAvgOilmass = 0.0;
                    if (totalMileage > 0 && totalOilmass > 0) {
                        totalAvgOilmass = totalOilmass * 100 / totalMileage;
                        BigDecimal b = new BigDecimal(totalAvgOilmass);
                        totalAvgOilmass = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    data.put("totalMileage", totalMileage);
                    data.put("totalOilmass", totalOilmass);
                    data.put("totalAvgOilmass", totalAvgOilmass);
                    
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("mileage", mileage);
                    map.put("oilmass", oilmass);
                    map.put("avgOilmass", avgOilmass);
                    data.put("d".concat(date.substring(6)), map);
                    // 这里需优化，改成update方式，todo
                    mongoDao.getMongoTemplate().save(data, gpsInfoReportCollectionName);

                    // 车辆总报表
                    Map<String, Object> report = new HashMap<String, Object>();
                    report.put("totalMileage", totalMileage);
                    report.put("totalOilmass", totalOilmass);
                    report.put("totalAvgOilmass", totalAvgOilmass);
                    carReportService.updateCarReport(date, device, report);

                    if (carId != null) {
                        String sql = "update car set total_mileage = total_mileage+" + mileage + " where id = ?";
                        this.jdbcTemplate.update(sql, carId);
                    }
                } catch (Exception e) {
                    logger.error("MileageAndOilmassDayReportTask error", e);
                }
            }
            if (list.size() < pageSize) {
                break;
            }
            pageNo++;
            list = deviceService.getDevices(df.parse(date).getTime(), pageSize, pageNo);
            if (list == null || list.isEmpty()) {
                break;
            }
        }

        logger.info("***********MileageAndOilmassDayReportTask end,time={} milliseconds********", System.currentTimeMillis() - startTime);
    }
}
