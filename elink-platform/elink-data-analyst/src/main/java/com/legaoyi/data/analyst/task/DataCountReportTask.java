package com.legaoyi.data.analyst.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.context.annotation.Lazy;
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
@Component("dataCountReportTask")
public class DataCountReportTask {

    private static final Logger logger = LoggerFactory.getLogger(DataCountReportTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private CarReportService carReportService;

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date yesterday = DateUtils.addDays(new Date(), -1);
        try {
            executeTask(df.format(yesterday));
        } catch (Exception e) {
            logger.error("dataCountReportTask error", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void executeTask(String date) throws Exception {
        logger.info("***********dataCountReportTask start,date={}********", date);
        long startTime = System.currentTimeMillis();
        int pageSize = 1000;
        int pageNo = 0;

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date currentDate = df.parse(date);
        List<?> list = deviceService.getDevices(currentDate.getTime(), pageSize, pageNo);
        if (list == null || list.isEmpty()) {
            return;
        }
        String month = date.substring(0, 6);
        String collectionName = "device_data_count_".concat(month);
        while (true) {
            for (Object o : list) {
                try {
                    Map<String, Object> device = (Map<String, Object>) o;
                    String deviceId = (String) device.get("deviceId");
                    String key = deviceId.concat(date.substring(4));
                    Cache cache = cacheManager.getCache("device_data_count_cache");
                    ValueWrapper value = cache.get(key);
                    if (value == null || value.get() == null) {
                        continue;
                    }

                    Map<String, Object> data = new HashMap<String, Object>();
                    String id = month.concat("_").concat(deviceId);
                    Query updateQuery = new Query();
                    // 按月每个设备一条统计信息
                    updateQuery.addCriteria(Criteria.where("_id").is(id));
                    Update update = new Update();
                    update.set("d".concat(date.substring(6)), value.get());
                    UpdateResult result = mongoDao.getMongoTemplate().updateFirst(updateQuery, update, collectionName);
                    if (result.getMatchedCount() == 0) {
                        data.put("_id", id);
                        data.put("d".concat(date.substring(6)), value.get());
                        data.put("deviceId", deviceId);
                        data.put("enterpriseId", device.get("enterpriseId"));
                        String carId = (String) device.get("carId");
                        if (carId != null) {
                            data.put("carId", carId);
                        }
                        mongoDao.getMongoTemplate().insert(data, collectionName);
                    }

                    cache.evict(key);

                    ValueWrapper gpsWrapper = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_GPS_CACHE).get(deviceId);
                    if (gpsWrapper != null) {
                        Map<?, ?> gps = (Map<?, ?>) gpsWrapper.get();
                        Map<String, Object> maxSpeedOverView = (Map<String, Object>) gps.get("maxSpeedOverView");
                        if (maxSpeedOverView != null) {
                            Map<String, Object> monthMaxspeed = (Map<String, Object>) maxSpeedOverView.get("month");
                            if (monthMaxspeed != null) {
                                long time = (Long) monthMaxspeed.get("time");
                                if (Integer.parseInt(df.format(new Date(time))) <= Integer.parseInt(date)) {
                                    double maxSpeed = (Double) monthMaxspeed.get("speed");
                                    // 车辆总报表
                                    Map<String, Object> report = new HashMap<String, Object>();
                                    report.put("maxSpeed", maxSpeed);
                                    carReportService.updateCarReport(date, device, report);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("dataCountReportTask error", e);
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
        logger.info("***********dataCountReportTask end,time={} milliseconds********", System.currentTimeMillis() - startTime);
    }
}
