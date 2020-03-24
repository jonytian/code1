package com.legaoyi.message.ext.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("jtt0001MessageExtendService")
public class JTT0001MessageExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "jtt808_0001_msg";
    }

    @Override
    public Object get(Object id) throws Exception {
        return this.mongoService.get(getEntityName(), String.valueOf(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form)
            throws Exception {
        String collectionName = getEntityName();
        /**
         * 查询条件 格式：{ "conditions":{ "simCode.eq":"013200000000" }, "rangeConditions":[ {
         * "fieldName":"alarmTime", "from":"2016-12-15 15:25:40", "includeLower":true,
         * "includeUpper":true, "to":"2016-12-17 15:25:40" } ] }
         */
        String enterpriseIdConditionKey = FIELD_NAME_ENTERPRISE_ID.concat(".eq");
        if (form.get(enterpriseIdConditionKey) != null) {
            Map<String, Object> conditions = (Map<String, Object>) form.get("conditions");
            if (conditions == null) {
                conditions = Maps.newHashMap();
                form.put("conditions", conditions);
            }
            conditions.put(enterpriseIdConditionKey, form.get(enterpriseIdConditionKey));
        }
        if (countable) {
            return this.mongoService.pageFind(collectionName, selects, orderBy, desc, pageSize, pageNo, form);
        }
        return this.mongoService.find(collectionName, selects, orderBy, desc, pageSize, pageNo, form);
    }
}
