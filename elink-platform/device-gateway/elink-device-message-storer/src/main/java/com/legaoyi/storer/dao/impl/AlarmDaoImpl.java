package com.legaoyi.storer.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.storer.dao.AlarmDao;
import com.legaoyi.storer.dao.MongoDao;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component("alarmDao")
public class AlarmDaoImpl implements AlarmDao {

    private static final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);

    private static final String INSERT_SQL = "insert into device_data_limit_alarm(id,enterprise_id,device_id,ip,type,alarm_time,create_time,gateway_id) values(?,?,?,?,?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao mongoDao;

    @Override
    public void saveDataLimitAlarm(final Map<String, Object> map) {
        jdbcTemplate.update(INSERT_SQL, new PreparedStatementSetter() {

            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, IdGenerator.nextIdStr());
                ps.setString(2, (String) map.get(Constants.MAP_KEY_ENTERPRISE_ID));
                ps.setString(3, (String) map.get(Constants.MAP_KEY_DEVICE_ID));
                ps.setString(4, (String) map.get("ip"));
                ps.setInt(5, (Integer) map.get("type"));
                ps.setTimestamp(6, new java.sql.Timestamp((Long) map.get("alarmTime")));
                ps.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
                ps.setString(8, (String) map.get("gatewayId"));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void batchSave(final List<?> list) throws Exception {
        // 0002告警信息按天存储mongodb
        try {
            mongoDao.batchInsert(list, "alarm_info");
        } catch (Exception e) {
            logger.error("batch save mongoDB error,data={}", JsonUtil.covertObjectToString(list), e);
        }
    }

    @Override
    public void batchUpdate(Map<String, Map<String, Long>> data) throws Exception {
        for (String alarmId : data.keySet()) {
            Map<String, Long> map = data.get(alarmId);
            Query query = new Query();
            // 按月每个设备一条统计信息
            query.addCriteria(Criteria.where("id").is(alarmId));
            Update update = new Update();
            for (String key : map.keySet()) {
                update.set(key, map.get(key));
            }
            mongoDao.getMongoTemplate().updateFirst(query, update, "alarm_info");
        }
    }
}
