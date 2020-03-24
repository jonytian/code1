package com.legaoyi.storer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.legaoyi.common.util.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.dao.DeviceDownMessageDao;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

@Component("deviceDownMessageDao")
public class DeviceDownMessageDaoImpl implements DeviceDownMessageDao {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDownMessageDaoImpl.class);

    private static final String INSERT_SQL = "insert into device_down_message(message_id,device_id,message_seq,message_body,state,create_time,enterprise_id,id) values(?,?,?,?,?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setMessageState(int state, String id) throws Exception {
        String sql = "update device_down_message set state= ? where id= ? ";
        jdbcTemplate.update(sql, state, id);
    }

    @Override
    public void batchSave(final List<?> list) throws Exception {
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<?, ?> map = (Map<?, ?>) list.get(i);
                Map<?, ?> message = (Map<?, ?>) map.get("message");
                Map<?, ?> messageHeader = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_HEADER);
                Map<?, ?> messageBody = (Map<?, ?>) message.get(Constants.MAP_KEY_MESSAGE_BODY);

                ps.setString(1, (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID));
                ps.setString(2, (String) map.get(Constants.MAP_KEY_DEVICE_ID));
                ps.setInt(3, (Integer) messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ));
                ps.setString(4, messageBody == null ? "" : JsonUtil.covertObjectToString(messageBody));
                ps.setInt(5, 0);
                ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setString(7, (String) map.get(Constants.MAP_KEY_ENTERPRISE_ID));
                ps.setString(8, IdGenerator.nextIdStr());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public List<?> getOfflineMessage(String deviceId) throws Exception {
        String sql = "select id, message_id as messageId,message_body as messageBody from device_down_message where device_id=? and state=? order by create_time";
        try {
            return jdbcTemplate.queryForList(sql, deviceId, 1);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public void delMessage(String id) throws Exception {
        String sql = "delete from device_down_message where id=?";
        jdbcTemplate.update(sql, id);
    }

}
