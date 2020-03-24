package com.legaoyi.persistence.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public interface MongoDao<T> {

	public T findById(String id, Class<T> entityClass);

	public T findById(String id, Class<T> entityClass, String collectionName);

	public T findOne(Query query, Class<T> entityClass);

	public T findOne(Query query, Class<T> entityClass, String collectionName);

	public List<T> find(Query query, Class<T> entityClass);

	public List<T> find(Query query, Class<T> entityClass, String collectionName);

	public long count(Query query, String collectionName);
	
	public MongoTemplate getMongoTemplate();

}
