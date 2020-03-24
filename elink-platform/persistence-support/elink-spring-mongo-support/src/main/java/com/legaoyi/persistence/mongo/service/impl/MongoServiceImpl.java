package com.legaoyi.persistence.mongo.service.impl;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.legaoyi.persistence.mongo.dao.MongoDao;
import com.legaoyi.persistence.mongo.model.Pagination;
import com.legaoyi.persistence.mongo.service.MongoService;

@Service("mongoService")
public class MongoServiceImpl implements MongoService {

    @SuppressWarnings("rawtypes")
    @Autowired
    @Qualifier("mongoDao")
    private MongoDao<Map> mongoDao;

    @Override
    public Map<?, ?> get(String collectionName, String id) throws Exception {
        return this.mongoDao.findById(id, Map.class, collectionName);
    }

    @Override
    public long count(String collectionName, Map<String, Object> andCondition) throws Exception {
        Query query = new Query();
        setQuery(query, andCondition);
        return this.mongoDao.count(query, collectionName);
    }

    @Override
    public List<?> find(String collectionName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        Query query;
        if (selectFields != null && selectFields.length > 0) {
            Document dbObject = new Document();
            // dbObject.put("name", "zhangsan"); //查询条件
            Document fieldsObject = new Document();
            // 指定返回的字段
            for (String field : selectFields) {
                fieldsObject.put(field, true);
            }
            query = new BasicQuery(dbObject, fieldsObject);
        } else {
            query = new Query();
        }

        setQuery(query, andCondition);
        if (orderBy != null && !"".equals(orderBy)) {
            if (desc) {
                query.with(Sort.by(Direction.DESC, orderBy));
            } else {
                query.with(Sort.by(Direction.ASC, orderBy));
            }
        }

        int skip = pageSize * (pageNo - 1);
        query.skip(skip).limit(pageSize);
        return this.mongoDao.find(query, Map.class, collectionName);
    }

    @Override
    public Pagination pageFind(String collectionName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        Query query = new Query();
        setQuery(query, andCondition);
        int totalCount = (int) this.mongoDao.count(query, collectionName);
        return new Pagination(pageNo, pageSize, totalCount, this.find(collectionName, selectFields, orderBy, desc, pageSize, pageNo, andCondition));
    }

    public MongoTemplate getMongoTemplate() {
        return this.mongoDao.getMongoTemplate();
    }

    @SuppressWarnings("unchecked")
    public void setQuery(Query query, Map<String, Object> andCondition) {
        Map<String, Object> conditions = (Map<String, Object>) andCondition.get("conditions");
        List<Map<String, Object>> rangeConditions = (List<Map<String, Object>>) andCondition.get("rangeConditions");
        if (conditions != null) {
            for (String key : conditions.keySet()) {
                int index = key.lastIndexOf(".");
                String fieldName = key.substring(0, index);
                String opt = key.substring(index + 1);
                if (opt.equalsIgnoreCase("eq")) {
                    query.addCriteria(new Criteria(fieldName).is(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("gt")) {
                    query.addCriteria(new Criteria(fieldName).gt(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("lt")) {
                    query.addCriteria(new Criteria(fieldName).lt(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("gte")) {
                    query.addCriteria(new Criteria(fieldName).gte(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("lte")) {
                    query.addCriteria(new Criteria(fieldName).lte(conditions.get(key)));
                } else if (opt.equalsIgnoreCase("like")) {
                    query.addCriteria(new Criteria(fieldName).regex("/" + String.valueOf(conditions.get(key)) + "/"));
                } else if (opt.equalsIgnoreCase("rlike")) {
                    query.addCriteria(new Criteria(fieldName).regex("^" + conditions.get(key)));
                } else if (opt.equalsIgnoreCase("exists")) {
                    query.addCriteria(new Criteria(fieldName).exists(Boolean.valueOf(String.valueOf(conditions.get(key)))));
                } else if (opt.equalsIgnoreCase("neq")) {
                    query.addCriteria(new Criteria(fieldName).ne(conditions.get(key)).exists(true));
                }
            }
        }
        /**
         * 条件格式："rangeConditions": [ { "fieldName": "gpsTime", "from": 2016-12-12, "includeLower":true, "includeUpper": true, "to": 2016-12-13 } ]
         **/
        if (rangeConditions != null) {
            for (Map<String, Object> map : rangeConditions) {
                String fieldName = (String) map.get("fieldName");
                boolean includeLower = (Boolean) map.get("includeLower");
                boolean includeUpper = (Boolean) map.get("includeUpper");
                Object from = map.get("from");
                Object to = map.get("to");
                if (includeLower && includeUpper) {
                    query.addCriteria(new Criteria(fieldName).gte(from).lte(to));
                } else if (!includeLower && includeUpper) {
                    query.addCriteria(new Criteria(fieldName).gt(from).lte(to));
                }
                if (includeLower && !includeUpper) {
                    query.addCriteria(new Criteria(fieldName).gte(from).lt(to));
                }
                if (!includeLower && !includeUpper) {
                    query.addCriteria(new Criteria(fieldName).gt(from).lt(to));
                }
            }
        }
    }

}
