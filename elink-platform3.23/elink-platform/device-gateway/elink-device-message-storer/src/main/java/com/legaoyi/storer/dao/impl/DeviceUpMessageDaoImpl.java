package com.legaoyi.storer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.legaoyi.storer.dao.DeviceUpMessageDao;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.JsonUtil;

@Component("deviceUpMessageDao")
public class DeviceUpMessageDaoImpl implements DeviceUpMessageDao {

    private static final Logger logger = LoggerFactory.getLogger(DeviceUpMessageDaoImpl.class);

    private static final String INSERT_SQL = "insert into device_up_message(message_id,device_id,message_seq,message_body,create_time,enterprise_id,id) values(?,?,?,?,?,?,?)";

    private static final String UPDATE_SQL = "update device_down_message set state= ? where device_id = ? and message_id=? and message_seq = ? and create_time > ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Map<String, String> respMessageMap = new HashMap<String, String>();
    static {
        respMessageMap.put("0104", "8104");
        respMessageMap.put("0201", "8201");
        respMessageMap.put("0302", "8302");
        respMessageMap.put("0700", "8700");
        respMessageMap.put("0802", "8802");
        respMessageMap.put("0805", "8801");
    }

    @Override
    public void batchSave(final List<?> list) {
        final List<Map<String, Object>> respList = new ArrayList<Map<String, Object>>();
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object o = list.get(i);
                ExchangeMessage message = (ExchangeMessage) o;
                Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
                String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
                Map<?, ?> m = (Map<?, ?>) message.getMessage();
                Map<?, ?> messageHeader = (Map<?, ?>) m.get(Constants.MAP_KEY_MESSAGE_HEADER);
                Map<?, ?> messageBody = (Map<?, ?>) m.get(Constants.MAP_KEY_MESSAGE_BODY);
                String respMessageId = null;
                Integer respMessageSeq = null;
                if (messageBody != null) {
                    respMessageId = (String) messageBody.get(Constants.MAP_KEY_MESSAGE_ID);
                    respMessageSeq = (Integer) messageBody.get(Constants.MAP_KEY_MESSAGE_SEQ);
                }

                String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
                if (respMessageSeq != null) {
                    if (respMessageId == null) {
                        respMessageId = respMessageMap.get(messageId);
                    }
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
                    map.put(Constants.MAP_KEY_MESSAGE_ID, respMessageId);
                    map.put(Constants.MAP_KEY_MESSAGE_SEQ, respMessageSeq);
                    respList.add(map);
                }
                ps.setString(1, messageId);
                ps.setString(2, deviceId);
                ps.setInt(3, (Integer) messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ));
                // 这里需要更新对应的下发指令的状态
                ps.setString(4, messageBody == null ? "" : JsonUtil.covertObjectToString(messageBody));
                ps.setTimestamp(5, new java.sql.Timestamp(message.getCreateTime()));
                ps.setString(6, (String) ((Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE)).get(Constants.MAP_KEY_ENTERPRISE_ID));
                ps.setString(7, IdGenerator.nextIdStr());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });

        if (!respList.isEmpty()) {
            batchUpdate(respList);
        }
    }

    @Override
    public void batchUpdate(List<?> list) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        jdbcTemplate.batchUpdate(UPDATE_SQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<?, ?> map = (Map<?, ?>) list.get(i);
                Integer result = (Integer) map.get("result");
                if (result == null) {
                    result = 5;
                } else {
                    result = result + 5;
                }
                ps.setInt(1, result);
                ps.setString(2, (String) map.get(Constants.MAP_KEY_DEVICE_ID));
                ps.setString(3, (String) map.get(Constants.MAP_KEY_MESSAGE_ID));
                ps.setInt(4, (Integer) map.get(Constants.MAP_KEY_MESSAGE_SEQ));
                try {
                    ps.setTimestamp(5, new java.sql.Timestamp(df.parse(df.format(new Date())).getTime()));
                } catch (ParseException e) {
                    logger.error("parse time error", e);
                }
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public void updateFileUploadState(String deviceId, int respMessageSeq, int state, int messageSeq) {
        String sql = "update device_history_media set state = ?,message_seq = ? where device_id=? and message_seq=?";
        jdbcTemplate.update(sql, state, messageSeq, deviceId, respMessageSeq);
    }
}
