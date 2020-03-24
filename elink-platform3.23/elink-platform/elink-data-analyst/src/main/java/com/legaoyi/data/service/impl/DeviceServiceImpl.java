package com.legaoyi.data.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import com.legaoyi.data.service.DeviceService;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    private static final String sql =
            "SELECT a.id as deviceId,a.state as state,b.id as carId,last_offline_time as lastOfflineTime,last_online_time as lastOnlineTime,a.enterprise_id as enterpriseId FROM device a left join car b on a.id = b.device_id WHERE a.state=3 OR (a.state=2 AND last_offline_time>?) ORDER BY a.enterprise_id  LIMIT ?,?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<?> getDevices(long lastOfflineTime, int pageSize, int pageNo) throws Exception {
        return jdbcTemplate.query(sql, new Object[] {lastOfflineTime, pageNo, pageSize}, new RowMapper<Map<String, Object>>() {

            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("deviceId", rs.getString("deviceId"));
                m.put("carId", rs.getString("carId"));
                m.put("state", rs.getShort("state"));
                m.put("lastOfflineTime", rs.getLong("lastOfflineTime"));
                m.put("lastOnlineTime", rs.getLong("lastOnlineTime"));
                m.put("enterpriseId", rs.getString("enterpriseId"));
                return m;
            }
        });
    }

    public List<?> getEnterprises() throws Exception {
        String sql = "SELECT DISTINCT enterprise_id as enterpriseId FROM device";
        return jdbcTemplate.query(sql, new Object[] {}, new RowMapper<String>() {

            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("enterpriseId");
            }
        });
    }

    @Override
    public void updateLongOffline(int state, long time) throws Exception {
        String sql = "update device set biz_state = ? where last_offline_time <=? and state!=3 ";
        jdbcTemplate.update(sql, state, time);
    }
}
