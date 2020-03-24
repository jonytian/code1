package com.legaoyi.lbs.ext.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("gpsExtendService")
public class GpsExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "gps_info_";
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form)
            throws Exception {
        // 检查时间范围todo
        String collectionName = getEntityName().concat(String.valueOf(form.get("recordDate")).replaceAll("-", ""));
        /**
         * 查询条件 格式：{ "conditions":{ "simCode.eq":"013200000000" }, "rangeConditions":[ {
         * "fieldName":"alarmTime", "from":"2016-12-15 15:25:40", "includeLower":true,
         * "includeUpper":true, "to":"2016-12-17 15:25:40" } ] }
         */
        // 限制只能根据设备id查询
        Map<?, ?> conditions = (Map<?, ?>) form.get("conditions");
        if (conditions == null || conditions.get("deviceId.eq") == null) {
            throw new BizProcessException("非法请求参数！");
        }

        // String enterpriseIdConditionKey = "enterpriseId".concat(".eq");
        // if (form.get(enterpriseIdConditionKey) != null) {
        // Map<String, Object> conditions = (Map<String, Object>) form.get("conditions");
        // if (conditions == null) {
        // conditions = new HashMap<String, Object>();
        // form.put("conditions", conditions);
        // }
        // conditions.put(enterpriseIdConditionKey, form.get(enterpriseIdConditionKey));
        // }
        if (countable) {
            return this.mongoService.pageFind(collectionName, selects, orderBy, desc, pageSize, pageNo, form);
        }
        return this.mongoService.find(collectionName, selects, orderBy, desc, pageSize, pageNo, form);
    }
}
