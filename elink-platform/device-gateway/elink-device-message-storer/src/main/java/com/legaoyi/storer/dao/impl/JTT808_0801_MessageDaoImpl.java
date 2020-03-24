package com.legaoyi.storer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.storer.dao.GeneralDao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801" + Constants.ELINK_MESSAGE_STORER_MESSAGE_DAO_BEAN_SUFFIX)
public class JTT808_0801_MessageDaoImpl implements GeneralDao {

    private static final String INSERT_SQL =
            "insert into device_history_media(id,enterprise_id,device_id,message_seq,biz_type,biz_id,location_type,event_code,resource_type,channel_id,file_path,state,create_time,gps_info) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void batchSave(final List<?> list) {
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object o = list.get(i);
                ExchangeMessage message = (ExchangeMessage) o;

                Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
                Map<?, ?> map = (Map<?, ?>) message.getMessage();
                Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
                Map<?, ?> messageBody = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

                Object id = messageBody.get("_id");
                if (id == null) {
                    ps.setString(1, IdGenerator.nextIdStr());
                } else {
                    ps.setString(1, String.valueOf(id));
                }

                ps.setString(2, (String) device.get(Constants.MAP_KEY_ENTERPRISE_ID));
                ps.setString(3, (String) device.get(Constants.MAP_KEY_DEVICE_ID));
                ps.setInt(4, (Integer) messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ));
                Integer bizType = (Integer) messageBody.get("bizType");
                ps.setInt(5, bizType == null ? 1 : bizType);
                Object bizId = messageBody.get("bizId");
                if (bizId != null) {
                    ps.setString(6, String.valueOf(bizId));
                } else {
                    ps.setString(6, "-1");
                }
                ps.setInt(7, 1);

                Object eventCode = messageBody.get("eventCode");
                if (eventCode != null) {
                    ps.setInt(8, (Integer) eventCode);
                } else {
                    ps.setInt(8, -1);
                }
                int mediaType = (Integer) messageBody.get("mediaType");
                ps.setInt(9, (mediaType == 0 ? 4 : mediaType));
                ps.setInt(10, (Integer) messageBody.get("channelId"));
                ps.setString(11, (String) messageBody.get("filePath"));
                ps.setInt(12, 3);
                ps.setTimestamp(13, new java.sql.Timestamp(message.getCreateTime()));
                Object gpsInfo = messageBody.get("gpsInfo");
                if (gpsInfo != null) {
                    ps.setString(14, JsonUtil.covertObjectToString(gpsInfo));
                } else {
                    ps.setString(14, "");
                }
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
