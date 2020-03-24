package com.legaoyi.data.analyst.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.DateUtils;
import com.legaoyi.common.util.JsonUtil;

@Lazy(false)
@EnableScheduling
@Component("alarmNotifyTask")
public class AlarmNotifyTask {

    private static final Logger logger = LoggerFactory.getLogger(AlarmNotifyTask.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Scheduled(cron = "0 0 2 * * ?")
    public void run() {
        executeTask();
    }

    public void executeTask() {
        logger.info("***********AlarmNotifyTask start********");
        long startTime = System.currentTimeMillis();
        // 查询企业的提醒设置规则
        String sql = "select enterprise_id as enterpriseId,content from system_config_dictionary where type = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, 41);
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> map : list) {
                String enterpriseId = (String) map.get("enterpriseId");
                String content = (String) map.get("content");
                List<?> rules = JsonUtil.convertStringToObject(content, List.class);
                if (rules != null && !rules.isEmpty()) {
                    for (Object o : rules) {
                        try {
                            Map<?, ?> rule = (Map<?, ?>) o;
                            int type = (Integer) rule.get("type");
                            if (type == 1) {
                                // 驾照到期提示
                                Object day = rule.get("rule");
                                if (day != null) {
                                    checkDriverLicense(enterpriseId, (Integer) day);
                                }
                            } else if (type == 2) {
                                // 年检到期提示
                                Object day = rule.get("rule");
                                if (day != null) {
                                    checkCarVerificationDate(enterpriseId, (Integer) day);
                                }
                            } else if (type == 3) {
                                // 行驶证到期提示：
                                Object day = rule.get("rule");
                                if (day != null) {
                                    checkCarLicenseDate(enterpriseId, (Integer) day);
                                }
                            } else if (type == 4) {
                                // 车辆保养提示：
                                List<?> mileages = (List<?>) rule.get("rule");
                                if (mileages != null && !mileages.isEmpty()) {
                                    for (Object mileage : mileages) {
                                        checkMileage(enterpriseId, (Integer) mileage);
                                    }
                                }
                            } else if (type == 6) {
                                // 车辆报废提示：
                                Object mileage = rule.get("rule");
                                if (mileage != null) {
                                    checkScrapCar(enterpriseId, ((Integer) mileage) * 10000);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("******AlarmNotifyTask error", e);
                        }
                    }
                }

            }
        }
        logger.info("***********AlarmNotifyTask end,time={} milliseconds********", System.currentTimeMillis() - startTime);
    }

    private void checkDriverLicense(String enterpriseId, int day) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(DateUtils.addDay(new Date(), day));
        String sql = "INSERT INTO system_alarm (id, enterprise_id, type, title, content, user_id, state, create_time )" + " SELECT ( SELECT REPLACE (UUID(), '-', '')) AS id, enterprise_id, 4 AS type, '驾照到期提醒' AS title, CONCAT( '您的驾照将在" + day
                + "天后(', license_end_date, ')到期，请及时更换驾照！' ) AS content, user_id AS userId, 0 AS state, now() AS createTime FROM officers_car_driver WHERE license_end_date > CURRENT_DATE () AND license_end_date = ? AND enterprise_id = ? ";
        jdbcTemplate.update(sql, date, enterpriseId);
    }

    private void checkCarVerificationDate(String enterpriseId, int day) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(DateUtils.addDay(new Date(), day));
        String sql = "INSERT INTO system_alarm ( id, enterprise_id, type, title, content, user_id, state, create_time ) "
                + "  SELECT ( SELECT REPLACE (UUID(), '-', '')) AS id, a.enterprise_id, 3 AS type, '年检到期提醒' AS title, CONCAT( plate_number, '年检将在\" + DAY + \"天后(', verification_date, ')到期，请及时对车辆进行年检！' ) AS content, b.id AS userId, 0 AS state, now() AS createTime"
                + "  FROM car a, security_user b WHERE a.enterprise_id = b.enterprise_id AND b.type = 2 "
                + "  AND verification_date > CURRENT_DATE () AND verification_date = ? AND a.enterprise_id = ?";
        jdbcTemplate.update(sql, date, enterpriseId);
    }

    private void checkCarLicenseDate(String enterpriseId, int day) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(DateUtils.addDay(new Date(), day));
        String sql = "INSERT INTO system_alarm ( id, enterprise_id, type, title, content, user_id, state, create_time ) "
                + "  SELECT ( SELECT REPLACE (UUID(), '-', '')) AS id, a.enterprise_id, 3 AS type, '行驶证到期提醒' AS title, CONCAT( plate_number, '行驶证将在\" + DAY + \"天后(', driving_license_date, ')到期，请及时处理！' ) AS content, b.id AS userId, 0 AS state, now() AS createTime"
                + "  FROM car a, security_user b WHERE a.enterprise_id = b.enterprise_id AND b.type = 2 "
                + "  AND driving_license_date > CURRENT_DATE () AND driving_license_date = ? AND a.enterprise_id = ?";
        jdbcTemplate.update(sql, date, enterpriseId);
    }

    private void checkMileage(String enterpriseId, int mileage) {
        String sql = "INSERT INTO system_alarm ( id, enterprise_id, type, title, content, user_id, state, create_time ) "
                + "  SELECT ( SELECT REPLACE (UUID(), '-', '')) AS id, a.enterprise_id, 4 AS type, '保养到期提醒' AS title, CONCAT( plate_number, '已运行', a.mileage, '公里，请及时对车辆进行保养！' ) AS content, b.id AS userId, 0 AS state, now() AS createTime"
                + "  FROM ( SELECT a.plate_number, ( COALESCE (a.initial_mileage, 0) + COALESCE (a.total_mileage, 0)) AS mileage, a.enterprise_id FROM car a "
                + "  WHERE  ( COALESCE (a.initial_mileage, 0) + COALESCE (a.total_mileage, 0)) > ? AND a.enterprise_id = ? ) a, security_user b"
                + "  WHERE a.enterprise_id = b.enterprise_id AND b.type=2 AND NOT EXISTS ( SELECT 1 FROM officers_car_notify_rule_record e WHERE e.type = 4 AND e.last_alarm_val >= a.mileage )";
        jdbcTemplate.update(sql, mileage, enterpriseId);

        // 更新提醒规则记录，避免重复提醒
        sql = "UPDATE officers_car_notify_rule_record SET last_alarm_val = ? WHERE EXISTS ( SELECT 1 FROM car b WHERE b.total_mileage > last_alarm_val AND b.id = car_id AND b.total_mileage > ? ) AND last_alarm_val < ? AND enterprise_id = ?";
        jdbcTemplate.update(sql, mileage, mileage, mileage, enterpriseId);

        // 记录提醒规则，避免重复提醒
        sql = "INSERT INTO officers_car_notify_rule_record ( id, enterprise_id, car_id, type, last_alarm_val, create_time, modify_time ) SELECT ( SELECT REPLACE (UUID(), '-', '')) AS id, a.enterprise_id, a.id, 4 AS type, " + mileage
                + " AS val, now() AS createTime, now() AS modify_time FROM car a WHERE NOT EXISTS ( SELECT 1 FROM officers_car_notify_rule_record b WHERE b.type = 4 AND b.car_id = a.id ) AND a.total_mileage > ? AND a.enterprise_id = ?";
        jdbcTemplate.update(sql, mileage, enterpriseId);
    }

    private void checkScrapCar(String enterpriseId, int mileage) {
        String sql =
                "INSERT INTO system_alarm ( id, enterprise_id, type, title, content, user_id, state, create_time ) SELECT ( SELECT REPLACE (UUID(), '-', '')) AS id, a.enterprise_id, 7 AS type, '车辆报废提醒' AS title, CONCAT( plate_number, '已经达到报废公里数，请及时报废车辆！' ) AS content, b.id AS userId, 0 AS state, now() AS createTime "
                        + "  FROM ( SELECT a.plate_number, a.enterprise_id FROM car a WHERE COALESCE (a.initial_mileage, 0) + COALESCE (a.total_mileage, 0) > ? AND a.enterprise_id = ? ) a, security_user b"
                        + "  WHERE a.enterprise_id = b.enterprise_id AND b.type = 2";
        jdbcTemplate.update(sql, mileage, enterpriseId);
    }
}
