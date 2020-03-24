package com.legaoyi.data.analyst.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.data.mongo.MongoDao;
import com.mongodb.BasicDBObject;


@Lazy(false)
@EnableScheduling
@Component("gpsInfoIndexTask")
public class GpsInfoIndexTask {

    private static final Logger logger = LoggerFactory.getLogger(GpsInfoIndexTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        try {
            String today = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
            executeTask(today);
        } catch (Exception e) {
            logger.error("GpsInfoIndexTask error", e);
        }
    }

    public void executeTask(String date) throws Exception {
        logger.info("***********GpsInfoIndexTask start,date={}********", date);
        String collectionName = "gps_info_".concat(date);
        if (!mongoDao.getMongoTemplate().collectionExists(collectionName)) {
            try {
                mongoDao.getMongoTemplate().createCollection(collectionName);
            } catch (Exception e) {
            }
        }
        BasicDBObject basicDBObject = new BasicDBObject("deviceId", 1);
        basicDBObject.put("gpsTime", 1);
        mongoDao.getMongoTemplate().getCollection(collectionName).createIndex(basicDBObject);

        collectionName = "alarm_info_".concat(date);
        if (!mongoDao.getMongoTemplate().collectionExists(collectionName)) {
            try {
                mongoDao.getMongoTemplate().createCollection(collectionName);
            } catch (Exception e) {
            }
        }
        mongoDao.getMongoTemplate().getCollection(collectionName).createIndex(new BasicDBObject("deviceId", 1));
        logger.info("***********GpsInfoIndexTask end********");
    }
}
