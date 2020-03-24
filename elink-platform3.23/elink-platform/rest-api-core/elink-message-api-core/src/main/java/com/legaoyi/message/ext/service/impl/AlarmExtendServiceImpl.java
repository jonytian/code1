package com.legaoyi.message.ext.service.impl;

import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.legaoyi.platform.ext.service.impl.DefaultMongoExtendServiceImpl;

/**
 * @author gaoshengbo
 */
@Service("alarmExtendService")
public class AlarmExtendServiceImpl extends DefaultMongoExtendServiceImpl {

    @Override
    protected String getEntityName() {
        return "alarm_info";
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        for (String key : entity.keySet()) {
            update.set(key, entity.get(key));
        }
        return mongoService.getMongoTemplate().updateFirst(query, update, getEntityName());
    }
}
