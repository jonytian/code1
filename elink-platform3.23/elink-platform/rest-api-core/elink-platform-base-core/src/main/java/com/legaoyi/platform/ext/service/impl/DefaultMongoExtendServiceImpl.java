package com.legaoyi.platform.ext.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.google.common.collect.Maps;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.persistence.mongo.service.MongoService;
import com.legaoyi.platform.ext.service.ExtendService;

public abstract class DefaultMongoExtendServiceImpl implements ExtendService {

    public static final String FIELD_NAME_ENTERPRISE_ID = "enterpriseId";

    @Autowired
    @Qualifier("mongoService")
    protected MongoService mongoService;

    protected abstract String getEntityName();

    @Override
    public Object get(Object id) throws Exception {
        throw new BizProcessException("不支持此操作");
    }

    @SuppressWarnings("unchecked")
    @Override
    public long count(Map<String, Object> form) throws Exception {
        String collectionName = getEntityName();
        if (form.get("recordDate") != null) {
            collectionName = collectionName.concat(String.valueOf(form.get("recordDate")).replaceAll("-", ""));
        }
        /**
         * 查询条件 格式：{ "conditions":{ "simCode.eq":"013200000000" }, "rangeConditions":[ { "fieldName":"alarmTime", "from":"2016-12-15 15:25:40",
         * "includeLower":true, "includeUpper":true, "to":"2016-12-17 15:25:40" } ] }
         */

        Map<String, Object> conditions = (Map<String, Object>) form.get("conditions");
        if (conditions == null) {
            conditions = Maps.newHashMap();
            form.put("conditions", conditions);
        }

        for (String key : form.keySet()) {
            if (key.startsWith(FIELD_NAME_ENTERPRISE_ID)) {
                conditions.put(key, form.get(key));
            }
        }
        return this.mongoService.count(collectionName, form);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception {
        // 检查时间范围todo
        String collectionName = getEntityName();
        if (form.get("recordDate") != null) {
            collectionName = collectionName.concat(String.valueOf(form.get("recordDate")).replaceAll("-", ""));
        }
        /**
         * 查询条件 格式：{ "conditions":{ "simCode.eq":"013200000000" }, "rangeConditions":[ { "fieldName":"alarmTime", "from":"2016-12-15 15:25:40",
         * "includeLower":true, "includeUpper":true, "to":"2016-12-17 15:25:40" } ] }
         */
        Map<String, Object> conditions = (Map<String, Object>) form.get("conditions");
        if (conditions == null) {
            conditions = Maps.newHashMap();
            form.put("conditions", conditions);
        }

        for (String key : form.keySet()) {
            if (key.startsWith(FIELD_NAME_ENTERPRISE_ID)) {
                conditions.put(key, form.get(key));
            }
        }

        if (countable) {
            return this.mongoService.pageFind(collectionName, selects, orderBy, desc, pageSize, pageNo, form);
        }
        return this.mongoService.find(collectionName, selects, orderBy, desc, pageSize, pageNo, form);
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        throw new BizProcessException("不支持此操作");
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        throw new BizProcessException("不支持此操作");
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        throw new BizProcessException("不支持此操作");
    }

}
