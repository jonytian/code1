package com.legaoyi.data.analyst.task;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.legaoyi.data.mongo.MongoDao;

@Lazy(false)
@EnableScheduling
@Component("historyDataCleanTask")
public class HistoryDataCleanTask {

    private static final Logger logger = LoggerFactory.getLogger(GpsInfoIndexTask.class);

    @Value("${mongo.clean.collection}")
    private String collections;

    @Value("${mongo.clean.before.day}")
    private int beforeDay;

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment env;

    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        try {
            executeTask(df.format(new Date()));
        } catch (Exception e) {
            logger.error("HistoryDataCleanTask error", e);
        }
    }

    public void executeTask(String date) throws Exception {
        logger.info("***********HistoryDataCleanTask start,date={}********", date);
        if (StringUtils.isBlank(collections)) {
            return;
        }
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        String arr[] = collections.split(",");
        for (String name : arr) {
            String collection = null;
            String customBeforeDay = env.getProperty(String.format("mongo.clean.%s.before.day", name));
            if (!StringUtils.isBlank(customBeforeDay)) {
                int n = Integer.valueOf(customBeforeDay);
                collection = name.concat("_").concat(df.format(DateUtils.addDays(df.parse(date), -n)));
            } else {
                collection = name.concat("_").concat(df.format(DateUtils.addDays(df.parse(date), -beforeDay)));
            }
            logger.info("***********dropCollection,name={}********", collection);
            try {
                mongoDao.getMongoTemplate().dropCollection(collection);
            } catch (Exception e) {
                logger.error("dropCollection error", e);
            }
        }
        jdbcTemplate.execute("delete from device_alarm_setting where datediff(now(),end_time) > 3");
        logger.info("***********HistoryDataCleanTask end********");
    }
}
