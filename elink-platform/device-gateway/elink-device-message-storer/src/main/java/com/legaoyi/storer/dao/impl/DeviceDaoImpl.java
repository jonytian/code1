package com.legaoyi.storer.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.storer.dao.DeviceDao;
import com.legaoyi.storer.dao.MongoDao;
import com.legaoyi.storer.util.Constants;

@Component("deviceDao")
public class DeviceDaoImpl implements DeviceDao {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao mongoDao;

    @Override
    public void batchSetDeviceStateOnline(final List<?> list) throws Exception {
        // 设备状态,0:未注册；1:已注册；2:离线；3:在线;4:已注销；5：已停用
        String sql = "update device set biz_state = ?,state = ? ,last_online_time = ? , last_gateway_id=? where sim_code=? and (last_offline_time < ? or last_offline_time is null) ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object o = list.get(i);
                Map<?, ?> map = (Map<?, ?>) o;
                Integer bizState = (Integer) map.get("bizState");
                ps.setInt(1, bizState == null ? 1 : bizState);
                ps.setInt(2, 3);
                ps.setLong(3, (Long) map.get("createTime"));
                ps.setString(4, (String) map.get("gatewayId"));
                ps.setString(5, (String) map.get("simCode"));
                ps.setLong(6, (Long) map.get("createTime"));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public void batchSetDeviceStateOffline(final List<?> list) throws Exception {
        String sql = "update device set biz_state = 0,state= ? ,last_offline_time = ? where sim_code=? and last_gateway_id=? and last_online_time < ? and state = 3 ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object o = list.get(i);
                Map<?, ?> map = (Map<?, ?>) o;
                ps.setInt(1, 2);
                ps.setLong(2, (Long) map.get("createTime"));
                ps.setString(3, (String) map.get("simCode"));
                ps.setString(4, (String) map.get("gatewayId"));
                ps.setLong(5, (Long) map.get("createTime"));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveDeviceOnOfflineLogs(List<?> list) throws Exception {
        mongoDao.batchInsert(list, "on_offline_logs_" + new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveDeviceAccStateLogs(List<?> list) throws Exception {
        mongoDao.batchInsert(list, "acc_state_logs_" + new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveDeviceParkingLogs(List<?> list) throws Exception {
        mongoDao.batchInsert(list, "device_parking_logs_" + new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()));
    }

    @Override
    public void setDeviceStateOffline(String gatewayId) throws Exception {
        String sql = "update device set biz_state = 0, state= ? ,last_offline_time = ? where state=? and last_gateway_id=?";
        jdbcTemplate.update(sql, 2, System.currentTimeMillis(), 3, gatewayId);
    }

    @Override
    public void setDeviceStateUnregistered(String simCode) throws Exception {
        String sql = "update device set state= ? ,last_offline_time = ? where  sim_code=?";
        jdbcTemplate.update(sql, 4, System.currentTimeMillis(), simCode);
    }

    @Override
    public void batchSetDeviceStateRegistered(final List<?> list) throws Exception {
        String sql = "update device set state=?,register_time=? where sim_code=?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object o = list.get(i);
                Map<?, ?> map = (Map<?, ?>) o;
                ps.setInt(1, 1);
                ps.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setString(3, (String) map.get("simCode"));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    public void batchSetDeviceBizState(final List<?> list) throws Exception {
        String sql = "update device set biz_state=? where id=?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object o = list.get(i);
                Map<?, ?> map = (Map<?, ?>) o;
                ps.setInt(1, (Integer) map.get("state"));
                ps.setString(2, (String) map.get(Constants.MAP_KEY_DEVICE_ID));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public Map<String, Object> getDeviceInfo(String simCode) throws Exception {
        String sql =
                "select a.id as deviceId,b.id as carId,b.plate_number as plateNumber,b.plate_color as plateColor, b.initial_mileage as initialMileage,b.tank_capacity as tankCapacity,name as deviceName,a.enterprise_id as enterpriseId,auth_code as authCode,terminal_id as terminalId, a.state as state, protocol_version as protocolVersion,alarm_setting_enabled as alarmSettingEnabled from device a left join car b on a.id = b.device_id where sim_code=? limit 1";
        try {
            return jdbcTemplate.queryForMap(sql, simCode);
        } catch (Exception e) {
            // logger.error("", e);
        }
        return null;
    }

    public List<?> getAlarmSetting(String deviceId) throws Exception {
        String sql = "select id,name,type,setting, start_time as startTime, end_time as endTime from device_alarm_setting where device_id=? and end_time >=? and state=1 and biz_type=1 order by type";
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = sd.format(new Date());
        try {
            return jdbcTemplate.queryForList(sql, deviceId, now);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public void resetAlarmSetting(String deviceId) throws Exception {
        String sql = "update device set alarm_setting_enabled= ? where id=?";
        jdbcTemplate.update(sql, 0, deviceId);
    }

    @Override
    public void resetAlarmSetting(String id, long endTime) throws Exception {
        String sql = "update device_alarm_setting set end_time= ? where id=?";
        jdbcTemplate.update(sql, new java.sql.Timestamp(endTime), id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getEnterpriseConfig(String enterpriseId) throws Exception {
        String sql = "select alarm_setting as alarmSetting, message_num_limit as messageNumLimit,message_byte_limit as messageByteLimit,upstream_message_limit as upMessageLimit from security_enterprise where id=? limit 1";
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, enterpriseId);
            if (map != null && !map.isEmpty()) {
                String alarmSetting = (String) map.get("alarmSetting");
                if (StringUtils.isNotBlank(alarmSetting)) {
                    map.putAll(JsonUtil.convertStringToObject(alarmSetting, Map.class));
                }
                return map;
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }
}
