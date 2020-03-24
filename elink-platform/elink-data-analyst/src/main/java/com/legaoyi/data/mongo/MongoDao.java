package com.legaoyi.data.mongo;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

public interface MongoDao<T> {

    public MongoTemplate getMongoTemplate();

    public T save(T entity) throws Exception;

    public T save(T entity, String collectionName) throws Exception;

    public void batchSave(List<T> list) throws Exception;

    public void batchSave(List<T> list, String collectionName) throws Exception;
}
