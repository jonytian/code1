package com.legaoyi.persistence.mongo.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.legaoyi.persistence.mongo.dao.MongoDao;

@Component("mongoDao")
public class MongoDaoImpl<T> implements MongoDao<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public T findById(String id, Class<T> entityClass) {
        return this.mongoTemplate.findById(id, entityClass);
    }

    @Override
    public T findById(String id, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findById(id, entityClass, collectionName);
    }

    @Override
    public T findOne(Query query, Class<T> entityClass) {
        return this.mongoTemplate.findOne(query, entityClass);
    }

    @Override
    public T findOne(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.findOne(query, entityClass, collectionName);
    }

    @Override
    public List<T> find(Query query, Class<T> entityClass) {
        return this.mongoTemplate.find(query, entityClass);
    }

    @Override
    public List<T> find(Query query, Class<T> entityClass, String collectionName) {
        return this.mongoTemplate.find(query, entityClass, collectionName);
    }

    @Override
    public long count(Query query, String collectionName) {
        return this.mongoTemplate.count(query, collectionName);
    }

    @Override
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

}
