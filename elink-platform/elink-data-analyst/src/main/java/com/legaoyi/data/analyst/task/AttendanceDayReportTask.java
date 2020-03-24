package com.legaoyi.data.analyst.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.data.mongo.MongoDao;

@Lazy(false)
@EnableScheduling
@Component("attendanceDayReportTask")
public class AttendanceDayReportTask {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceDayReportTask.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private MongoDao mongoDao;

    @Scheduled(cron = "0 0 3 * * ?")
    public void run() {
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            Date yesterday = DateUtils.addDays(new Date(), -1);
            executeTask(df.format(yesterday));
        } catch (Exception e) {
            logger.error("attendanceDayReportTask error", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void executeTask(String date) throws Exception {
        logger.info("***********attendanceDayReportTask start,date={}********", date);
        long startTime = System.currentTimeMillis();
        String collectionName = "jtt808_0702_msg_".concat(date);
        int pageSize = 1000;
        int pageNo = 0;
        Document dbObject = new Document();
        Document fieldsObject = new Document();
        Query query = new BasicQuery(dbObject, fieldsObject);
        int skip = pageSize * (pageNo - 1);
        query.skip(skip).limit(pageSize);
        query.with(new Sort(Direction.ASC, "qualification", "icCardOptTime"));
        List<?> list = mongoDao.getMongoTemplate().find(query, Map.class, collectionName);
        if (list != null && !list.isEmpty()) {
            List<Map<String, Object>> infolist = new ArrayList<Map<String, Object>>();
            while (true) {
                String lastQualification = null;
                for (Object o : list) {
                    try {
                        Map<String, Object> map = (Map<String, Object>) o;
                        String qualification = (String) map.get("qualification");
                        if (qualification == null || qualification.trim().equals("")) {
                            continue;
                        }
                        boolean isSame = lastQualification != null && qualification.equals(lastQualification);
                        if (!isSame && !infolist.isEmpty()) {
                            this.handlerList(date, infolist);
                            infolist = new ArrayList<Map<String, Object>>();
                        }
                        infolist.add(map);
                        lastQualification = qualification;
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
                if (!infolist.isEmpty()) {
                    this.handlerList(date, infolist);
                }
                if (list.size() < pageSize) {
                    break;
                }
                pageNo++;
                skip = pageSize * (pageNo - 1);
                query.skip(skip).limit(pageSize);
                list = mongoDao.getMongoTemplate().find(query, Map.class, collectionName);
                if (list == null || list.isEmpty()) {
                    break;
                }
            }
        }
        logger.info("***********attendanceDayReportTask end,time={} milliseconds********", System.currentTimeMillis() - startTime);
    }

    private void handlerList(String date, List<Map<String, Object>> list) {
        Map<String, Object> last = null;
        int lastIcCardState = 0;
        String driverName = null;
        List<Map<String, Object>> formatList = new ArrayList<Map<String, Object>>();
        try {
            for (Map<String, Object> map : list) {
                int icCardState = (Integer) map.get("icCardState");
                if (last != null && icCardState == 2 && icCardState == lastIcCardState) {
                    // 补充一个上班卡
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.putAll(last);
                    m.put(("icCardState"), 1);
                    formatList.add(m);
                }
                if (driverName == null || driverName.trim().equals("")) {
                    driverName = (String) map.get("driverName");
                }
                formatList.add(map);
                last = map;
                lastIcCardState = icCardState;
            }

            int time = 0;
            String lastIcCardOptTime = null;
            Date currentDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            for (Map<String, Object> map : formatList) {
                String icCardOptTime = (String) map.get("icCardOptTime");
                // 1上班，2下班
                int icCardState = (Integer) map.get("icCardState");
                if (icCardState == 2) {
                    // 下班卡
                    if (lastIcCardOptTime == null) {
                        // 有下班记录，没有上班记录，可能跨天了
                        time += df.parse(icCardOptTime).getTime() - currentDate.getTime();
                    } else {
                        time += df.parse(icCardOptTime).getTime() - df.parse(lastIcCardOptTime).getTime();
                    }
                    lastIcCardOptTime = null;
                } else {
                    lastIcCardOptTime = icCardOptTime;
                }
            }

            if (lastIcCardOptTime != null) {
                // 没有下班卡，可能是跨天了
                Date nextDate = DateUtils.addDays(currentDate, 1);
                time += nextDate.getTime() - df.parse(lastIcCardOptTime).getTime();
            }
            if (time > 0) {
                // 保存
                String qualification = (String) last.get("qualification");
                String enterpriseId = (String) last.get("enterpriseId");
                this.save(date, enterpriseId, driverName, qualification, time);
                time = 0;
            }
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void save(String date, String enterpriseId, String driverName, String qualification, long time) {
        String month = date.substring(0, 6);
        String attendanceReportCollectionName = "attendance_day_report_".concat(month);
        String id = month.concat("_").concat(qualification);
        Map<String, Object> data = mongoDao.getMongoTemplate().findById(id, Map.class, attendanceReportCollectionName);
        if (data == null) {
            data = new HashMap<String, Object>();
            data.put("_id", id);
            data.put("qualification", qualification);
            data.put("driverName", driverName);
            data.put("enterpriseId", enterpriseId);
        }
        Long totalTime = (Long) data.get("total");
        if (totalTime == null) {
            totalTime = 0l;
        }
        totalTime += time;
        data.put("total", totalTime);
        data.put("d".concat(date.substring(6)), time);
        mongoDao.getMongoTemplate().save(data, attendanceReportCollectionName);
    }
}
