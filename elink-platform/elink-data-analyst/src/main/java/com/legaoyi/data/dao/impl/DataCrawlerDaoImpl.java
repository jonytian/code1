package com.legaoyi.data.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import com.legaoyi.data.dao.DataCrawlerDao;

@Component("dataCrawlerDao")
public class DataCrawlerDaoImpl implements DataCrawlerDao {

    private static final Logger logger = LoggerFactory.getLogger(DataCrawlerDaoImpl.class);

    public static final String INSERT_OIL_PRICE_SQL = "insert into system_oil_price(id,area,gasoline89,gasoline92,gasoline95,gasoline98,diesel_oil,update_time,create_time,area_code) values(?,?,?,?,?,?,?,?,?,?)";

    public static final String INSERT_PLATENO_LIMIT_RULE_SQL = "insert into system_plateno_limit_rule(id,area,area_code,date,rule,create_time) values(?,?,?,?,?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveOilPrices(Map<String, Object> data) {
        try {
            jdbcTemplate.update(INSERT_OIL_PRICE_SQL, new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, (String) data.get("id"));
                    ps.setString(2, (String) data.get("area"));
                    ps.setDouble(3, (Double) data.get("gasoline89"));
                    ps.setDouble(4, (Double) data.get("gasoline92"));
                    ps.setDouble(5, (Double) data.get("gasoline95"));
                    ps.setDouble(6, (Double) data.get("gasoline98"));
                    ps.setDouble(7, (Double) data.get("dieselOil"));
                    ps.setString(8, (String) data.get("date"));
                    ps.setTimestamp(9, new java.sql.Timestamp(new Date().getTime()));
                    ps.setString(10, (String) data.get("areaCode"));
                }

            });
        } catch (Exception e) {

        }

    }

    @Override
    public Map<String, Object> getArea(String area) {
        try {
            String sql = "SELECT area_code as areaCode,area_name as areaName FROM lbs_district where  area_name like ?  and area_level = 1 limit 1";
            return jdbcTemplate.queryForMap(sql, area.concat("%"));
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    @Override
    public void batchSavePlatenoLimitRule(final List<?> list) {

        jdbcTemplate.batchUpdate(INSERT_PLATENO_LIMIT_RULE_SQL, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map<?, ?> map = (Map<?, ?>) list.get(i);
                ps.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
                ps.setString(2, (String) map.get("area"));
                ps.setString(3, (String) map.get("areaCode"));
                ps.setString(4, (String) map.get("date"));
                ps.setString(5, (String) map.get("rule"));
                ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    @Override
    public void delPlatenoLimitRule(String areaCode) {
        jdbcTemplate.update("delete from system_plateno_limit_rule where area_code = ? ", areaCode);
    }

}
