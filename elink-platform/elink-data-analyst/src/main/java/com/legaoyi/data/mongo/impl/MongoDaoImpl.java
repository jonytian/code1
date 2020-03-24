package com.legaoyi.data.mongo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.legaoyi.data.mongo.MongoDao;

@Component("mongoDao")
public class MongoDaoImpl<T> implements MongoDao<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Override
    public T save(T entity) throws Exception {
        mongoTemplate.insert(entity);
        return entity;
    }

    @Override
    public T save(T entity, String collectionName) throws Exception {
        mongoTemplate.insert(entity, collectionName);
        return entity;
    }

    @Override
    public void batchSave(List<T> list, String collectionName) throws Exception {
        mongoTemplate.insert(list, collectionName);
    }

    @Override
    public void batchSave(List<T> list) throws Exception {
        mongoTemplate.insert(list);
    }
}
