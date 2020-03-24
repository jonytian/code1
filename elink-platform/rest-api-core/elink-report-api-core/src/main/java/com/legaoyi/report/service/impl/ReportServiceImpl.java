package com.legaoyi.report.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.AggregationSpELExpression;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.persistence.mongo.service.MongoService;
import com.legaoyi.report.service.ReportService;

/**
 * @author gaoshengbo MongoDB Aggregation Framework初探 <br/>
 *         •$project - SELECT <br/>
 *         •$match - WHERE/HAVING <br/>
 *         •$limit - LIMIT <br/>
 *         •$skip - 同上 <br/>
 *         •$unwind - 可以将一个包含数组的文档切分成多个, 比如你的文档有<br/>
 *         中有个数组字段 A, A中有10个元素, 那么 经过 $unwind处理后会产生10个文档，这些文档只有 字段 A不同 <br/>
 *         •$group - GROUP BY –$avg, $sum … 如{$group:{_id:’$province’,$avg:’$age’}} 相当于select province, avg(age) from student where province = '上海'
 *         group by province <br/>
 *         •$sort - 排序<br/>
 */
@Service("reportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    @Qualifier("mongoService")
    private MongoService mongoService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> enterpriseDailyAlarmOverview(String date, String enterpriseId) throws Exception {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date d = df.parse(date);
        long startTime = d.getTime();
        long endTime = startTime + 24 * 60 * 60 * 1000;

        String fields[] = new String[51];
        fields[50] = "enterpriseId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        Criteria criteria = Criteria.where(fields[fields.length - 1]).is(enterpriseId);
        criteria.andOperator(Criteria.where("alarmTime").gte(startTime).lt(endTime));

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, "alarm_info", Map.class);
        return result.getMappedResults();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> deviceDailyAlarmOverview(String date, String enterpriseId, String deviceId) throws Exception {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date d = df.parse(date);
        long startTime = d.getTime();
        long endTime = startTime + 24 * 60 * 60 * 1000;

        String fields[] = new String[51];
        fields[50] = "deviceId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        Criteria criteria = Criteria.where("enterpriseId").is(enterpriseId);
        criteria.andOperator(Criteria.where("deviceId").is(deviceId), Criteria.where("alarmTime").gte(startTime).lt(endTime));

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, "alarm_info", Map.class);
        return result.getMappedResults();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> deviceDailyAlarmOverviewList(String date, String enterpriseId, String deviceId, int pageNo, int pageSize) throws Exception {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date d = df.parse(date);
        long startTime = d.getTime();
        long endTime = startTime + 24 * 60 * 60 * 1000;

        String collectionName = "alarm_info";
        String fields[] = new String[51];
        fields[50] = "deviceId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        Criteria criteria = Criteria.where("enterpriseId").is(enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            criteria.andOperator(Criteria.where("deviceId").is(deviceId), Criteria.where("alarmTime").gte(startTime).lt(endTime));
        } else {
            criteria.andOperator(Criteria.where("alarmTime").gte(startTime).lt(endTime));
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.skip((long) (pageNo - 1) * pageSize), Aggregation.limit(pageSize), Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        return result.getMappedResults();
    }

    @Override
    public List<?> deviceOverView(String enterpriseId) throws Exception {
        return this.service.findBySql("SELECT state as state,count(1) as total from device where enterprise_id =? group by state", enterpriseId);
    }

    @Override
    public List<?> deviceTypeOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT type as type,count(1) as total from device where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " AND state >= 2 AND state <= 3 group by type";
        return this.service.findBySql(sql, enterpriseId);
    }

    @Override
    public List<?> deviceBizTypeOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT biz_state as bizState,count(1) as total from device where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " AND state >= 2 AND state <= 3 group by biz_state";
        return this.service.findBySql(sql, enterpriseId);
    }

    @Override
    public List<?> deviceTypeAndBizTypeOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT type as type,biz_state as bizState,count(1) as total from device where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " AND state >= 2 AND state <= 3 group by type,biz_state order by type,biz_state";
        return this.service.findBySql(sql, enterpriseId);
    }

    @Override
    public List<?> carBiztypeOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT biz_type as type,count(1) as total from car where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " group by biz_type";
        return this.service.findBySql(sql, enterpriseId);
    }

    @Override
    public List<?> deviceProtocolOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT protocol_version as protocol,count(1) as total from v_car_device where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " group by protocol_version";
        return this.service.findBySql(sql, enterpriseId);
    }

    @Override
    public List<?> provinceCarOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT province_code as areaCode,count(1) as total from car where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " group by province_code";
        return this.service.findBySql(sql, enterpriseId);
    }

    public List<?> cityCarOverView(String enterpriseId, boolean isParent, String provinceCode) throws Exception {
        String sql = "SELECT city_code as areaCode,count(1) as total from car where ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " AND province_code =? ";
        sql += " group by city_code";
        return this.service.findBySql(sql, enterpriseId, provinceCode);
    }

    @Override
    public List<?> carBiztypeAndBizStateOverView(String enterpriseId, boolean isParent) throws Exception {
        String sql = "SELECT biz_type AS type, biz_state AS bizState, count(1) AS total FROM v_car_device WHERE ";
        if (isParent) {
            sql += " enterprise_id like ? ";
            enterpriseId = enterpriseId + "%";
        } else {
            sql += " enterprise_id =? ";
        }
        sql += " AND device_state >= 2 AND device_state <= 3 GROUP BY biz_type, biz_state ORDER BY biz_type, biz_state ";
        return this.service.findBySql(sql, enterpriseId);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> enterpriseHistoryDailyAlarmOverview(String date, String enterpriseId) throws Exception {
        String fields[] = new String[51];
        fields[50] = "enterpriseId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where(fields[fields.length - 1]).is(enterpriseId).andOperator(Criteria.where("date").is(Integer.parseInt(date)))), groupOperation,
                Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());

        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, "alarm_day_report_".concat(date.substring(0, 6)), Map.class);
        return result.getMappedResults();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> deviceHistoryDailyAlarmOverview(String date, String enterpriseId, String deviceId) throws Exception {
        String fields[] = new String[51];
        fields[50] = "deviceId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where("enterpriseId").is(enterpriseId).andOperator(Criteria.where("date").is(Integer.parseInt(date)), Criteria.where(fields[fields.length - 1]).is(deviceId))),
                groupOperation, Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, "alarm_day_report_".concat(date.substring(0, 6)), Map.class);
        return result.getMappedResults();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> enterpriseMonthAlarmOverview(String date, String enterpriseId, boolean isParent) throws Exception {
        String collectionName = "alarm_day_report_".concat(date);
        String fields[] = new String[50];
        GroupOperation groupOperation = Aggregation.group();
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        Criteria criteria = null;
        if (isParent) {
            criteria = new Criteria("enterpriseId").regex("^" + enterpriseId);
        } else {
            criteria = Criteria.where("enterpriseId").is(enterpriseId);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.project(fields));
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        return result.getMappedResults();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> deviceMonthAlarmOverview(String date, String enterpriseId, String deviceId) throws Exception {
        String collectionName = "alarm_day_report_".concat(date);
        String fields[] = new String[51];
        fields[50] = "deviceId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }
        Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where("enterpriseId").is(enterpriseId).andOperator(Criteria.where(fields[fields.length - 1]).is(deviceId))), groupOperation,
                Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        return result.getMappedResults();
    }

    @Override
    public List<?> deviceMonthAlarmOverviewList(String date, String enterpriseId, String deviceId, int pageNo, int pageSize) throws Exception {
        String collectionName = "alarm_day_report_".concat(date);
        String fields[] = new String[51];
        fields[50] = "deviceId";
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < 50; i++) {
            String alarm = "a" + i;
            fields[i] = alarm;
            groupOperation = groupOperation.sum(alarm).as(alarm);
        }

        Criteria criteria = Criteria.where("enterpriseId").is(enterpriseId);
        if (!StringUtils.isBlank(deviceId)) {
            criteria.andOperator(Criteria.where(fields[fields.length - 1]).is(deviceId));
        }

        Aggregation agg =
                Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.skip((long) (pageNo - 1) * pageSize), Aggregation.limit(pageSize), Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<?> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        return result.getMappedResults();
    }

    @Override
    public List<?> monthGpsInfoOverview(String date, String deviceId) throws Exception {
        String collectionName = "gps_info_day_report_".concat(date);
        Date firstDate = DateUtils.parse(date.concat("01"), "yyyyMMdd");
        int firstDay = Integer.parseInt(DateUtils.getMonthFirstDay(firstDate).replaceAll("-", ""));
        int lastDay = Integer.parseInt(DateUtils.getMonthLastDay(firstDate).replaceAll("-", ""));
        String selectFields[] = {"oilmass", "mileage", "date"};
        Map<String, Object> form = new HashMap<String, Object>();
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put("deviceId.eq", deviceId);
        form.put("conditions", conditions);
        List<?> list = this.mongoService.find(collectionName, selectFields, "date", false, 31, 1, form);
        List<Object> results = new ArrayList<Object>();
        if (list != null && !list.isEmpty()) {
            Map<Integer, Object> data = new HashMap<Integer, Object>();
            for (Object o : list) {
                Map<?, ?> m = (Map<?, ?>) o;
                data.put((Integer) m.get("date"), m);
            }
            for (int i = firstDay; i <= lastDay; i++) {
                Object o = data.get(i);
                if (o != null) {
                    results.add(o);
                } else {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("date", i);
                    m.put("mileage", 0);
                    m.put("oilmass", 0);
                    results.add(m);
                }
            }
        } else {
            for (int i = firstDay; i <= lastDay; i++) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("date", i);
                m.put("mileage", 0);
                m.put("oilmass", 0);
                results.add(m);
            }
        }
        return results;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<?, ?> enterpriseMonthOfficersCarCostOverview(String date, String enterpriseId) throws Exception {
        String fields[] = new String[] {"oilmass", "parking", "road", "maintenance", "insurance", "other", "enterpriseId"};
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < fields.length - 1; i++) {
            groupOperation = groupOperation.sum(fields[i]).as(fields[i]);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where(fields[fields.length - 1]).is(enterpriseId)), groupOperation, Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, "car_cost_month_report_".concat(date), Map.class);
        if (result != null && !result.getMappedResults().isEmpty()) {
            return result.getMappedResults().get(0);
        }
        return Maps.newHashMap();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<?, ?> deviceMonthOfficersCarCostOverview(String date, String enterpriseId, String deviceId) throws Exception {
        String fields[] = new String[] {"oilmass", "parking", "road", "maintenance", "insurance", "other", "deviceId"};
        GroupOperation groupOperation = Aggregation.group(fields[fields.length - 1]);
        for (int i = 0; i < fields.length - 1; i++) {
            groupOperation = groupOperation.sum(fields[i]).as(fields[i]);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(Criteria.where("enterpriseId").is(enterpriseId).andOperator(Criteria.where(fields[fields.length - 1]).is(deviceId))), groupOperation,
                Aggregation.project(fields).and(fields[fields.length - 1]).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, "car_cost_month_report_".concat(date), Map.class);
        if (result != null && !result.getMappedResults().isEmpty()) {
            return result.getMappedResults().get(0);
        }
        return Maps.newHashMap();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<?, ?> sumByEnterprise(String entityName, String date, String[] selectFields, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception {
        GroupOperation groupOperation = Aggregation.group(enterpriseId);
        for (int i = 0; i < selectFields.length; i++) {
            groupOperation = groupOperation.sum(selectFields[i]).as(selectFields[i]);
        }

        Criteria criteria = null;
        if (isParent) {
            criteria = new Criteria("enterpriseId").regex("^" + enterpriseId);
        } else {
            criteria = Criteria.where("enterpriseId").is(enterpriseId);
        }
        if (form != null) {
            Criteria[] arr = paserConditions(form);
            if (arr.length > 0) {
                criteria.andOperator(arr);
            }
        }

        String collectionName = entityName;
        if (StringUtils.isNotEmpty(date)) {
            collectionName += "_".concat(date);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.project(selectFields).and("enterpriseId").previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        if (result != null && !result.getMappedResults().isEmpty()) {
            return result.getMappedResults().get(0);
        }
        return Maps.newHashMap();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<?> addAndSumByEnterprise(String entityName, String date, String group, String[] sumFields, int pageSize, int pageNo, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception {
        GroupOperation groupOperation = Aggregation.group(group);
        if (sumFields.length > 1) {
            String addStr = "";
            for (int i = 0; i < sumFields.length; i++) {
                addStr += "+ifNull(" + sumFields[i] + ",0)";
            }
            groupOperation = groupOperation.sum(AggregationSpELExpression.expressionOf(addStr.substring(1))).as("total");
        } else {
            groupOperation = groupOperation.sum(sumFields[0]).as("total");
        }

        Criteria criteria = null;
        if (isParent) {
            criteria = new Criteria("enterpriseId").regex("^" + enterpriseId);
        } else {
            criteria = Criteria.where("enterpriseId").is(enterpriseId);
        }
        if (form != null) {
            Criteria[] arr = paserConditions(form);
            if (arr.length > 0) {
                criteria.andOperator(arr);
            }
        }

        String collectionName = entityName;
        if (StringUtils.isNotEmpty(date)) {
            collectionName += "_".concat(date);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.sort(Direction.DESC, "total"), Aggregation.skip((long) (pageNo - 1) * pageSize), Aggregation.limit(pageSize),
                Aggregation.project(group, "total").and(group).previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        return result.getMappedResults();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<?, ?> countByEnterprise(String entityName, String date, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception {
        GroupOperation groupOperation = Aggregation.group(enterpriseId).count().as("total");

        Criteria criteria = null;
        if (isParent) {
            criteria = new Criteria("enterpriseId").regex("^" + enterpriseId);
        } else {
            criteria = Criteria.where("enterpriseId").is(enterpriseId);
        }
        if (form != null) {
            Criteria[] arr = paserConditions(form);
            if (arr.length > 0) {
                criteria.andOperator(arr);
            }
        }

        String collectionName = entityName;
        if (StringUtils.isNotEmpty(date)) {
            collectionName += "_".concat(date);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), groupOperation, Aggregation.project("total").and("enterpriseId").previousOperation());
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        if (result != null && !result.getMappedResults().isEmpty()) {
            return result.getMappedResults().get(0);
        }
        return Maps.newHashMap();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Map<?, ?> distinctCountByEnterprise(String entityName, String date, String distinct, String enterpriseId, Map<String, Object> form, boolean isParent) throws Exception {
        Criteria criteria = null;
        if (isParent) {
            criteria = new Criteria("enterpriseId").regex("^" + enterpriseId);
        } else {
            criteria = Criteria.where("enterpriseId").is(enterpriseId);
        }
        if (form != null) {
            Criteria[] arr = paserConditions(form);
            if (arr.length > 0) {
                criteria.andOperator(arr);
            }
        }

        String collectionName = entityName;
        if (StringUtils.isNotEmpty(date)) {
            collectionName += "_".concat(date);
        }

        Aggregation agg = Aggregation.newAggregation(Aggregation.match(criteria), Aggregation.group(distinct).count().as("total"), Aggregation.group(distinct).count().as("total"), Aggregation.project("total"));
        AggregationResults<Map> result = this.mongoTemplate.aggregate(agg, collectionName, Map.class);
        if (result != null && !result.getMappedResults().isEmpty()) {
            return result.getMappedResults().get(0);
        }
        return Maps.newHashMap();
    }

    @Override
    public List<?> alarmNotifyOverview(String enterpriseId, String userId) throws Exception {
        String sql = "SELECT type,count(1) as total FROM system_alarm WHERE  enterprise_id = ? AND user_id = ? AND state=0 GROUP BY type  ORDER BY type";
        return this.service.findBySql(sql, enterpriseId, userId);
    }

    @SuppressWarnings("unchecked")
    private Criteria[] paserConditions(Map<String, Object> andCondition) {
        List<Criteria> list = Lists.newArrayList();
        Map<String, Object> conditions = (Map<String, Object>) andCondition.get("conditions");
        if (conditions != null) {
            for (String key : conditions.keySet()) {
                int index = key.lastIndexOf(".");
                String fieldName = key.substring(0, index);
                String opt = key.substring(index + 1);
                if (opt.equalsIgnoreCase("eq")) {
                    list.add(new Criteria(fieldName).is(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("gt")) {
                    list.add(new Criteria(fieldName).gt(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("lt")) {
                    list.add(new Criteria(fieldName).lt(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("gte")) {
                    list.add(new Criteria(fieldName).gte(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("lte")) {
                    list.add(new Criteria(fieldName).lte(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("like")) {
                    list.add(new Criteria(fieldName).regex("/" + String.valueOf(conditions.get(key)) + "/"));
                } else if (opt.equalsIgnoreCase("rlike")) {
                    list.add(new Criteria(fieldName).regex("^" + conditions.get(key)));
                } else if (opt.equalsIgnoreCase("exists")) {
                    list.add(new Criteria(fieldName).exists(Boolean.valueOf(String.valueOf(conditions.get(key)))));
                } else if (opt.equalsIgnoreCase("neq")) {
                    list.add(new Criteria(fieldName).ne(conditions.get(key)).exists(true));
                }
            }
        }
        /**
         * 条件格式："rangeConditions": [ { "fieldName": "gpsTime", "from": 2016-12-12, "includeLower":true, "includeUpper": true, "to": 2016-12-13 } ]
         **/

        List<Map<String, Object>> rangeConditions = (List<Map<String, Object>>) andCondition.get("rangeConditions");
        if (rangeConditions != null) {
            for (Map<String, Object> map : rangeConditions) {
                String fieldName = (String) map.get("fieldName");
                boolean includeLower = (Boolean) map.get("includeLower");
                boolean includeUpper = (Boolean) map.get("includeUpper");
                Object from = map.get("from");
                Object to = map.get("to");
                if (includeLower && includeUpper) {
                    list.add(new Criteria(fieldName).gte(from).lte(to));
                } else if (!includeLower && includeUpper) {
                    list.add(new Criteria(fieldName).gt(from).lte(to));
                }
                if (includeLower && !includeUpper) {
                    list.add(new Criteria(fieldName).gte(from).lt(to));
                }
                if (!includeLower && !includeUpper) {
                    list.add(new Criteria(fieldName).gt(from).lt(to));
                }
            }
        }
        Criteria[] criteria = new Criteria[list.size()];
        int index = 0;
        for (Criteria c : list) {
            criteria[index++] = c;
        }
        return criteria;
    }
}
