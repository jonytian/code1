package com.legaoyi.persistence.mongo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.legaoyi.persistence.mongo.model.Pagination;

public interface MongoService {

    /**
     *
     * @param collectionName
     * @param id
     * @return Map
     * @throws Exception
     */
    public Map<?, ?> get(String collectionName, String id) throws Exception;

    /**
     * 
     * @param collectionName
     * @param andCondition
     * @return
     * @throws Exception
     */
    public long count(String collectionName, Map<String, Object> andCondition) throws Exception;

    /**
     *
     * @param collectionName
     * @param selectFields
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param andCondition
     * @return List
     * @throws Exception
     */
    public List<?> find(String collectionName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     *
     * @param collectionName
     * @param selectFields
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param andCondition
     * @return Pagination
     * @throws Exception
     */
    public Pagination pageFind(String collectionName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param query
     * @param andCondition
     * @throws Exception
     */
    public void setQuery(Query query, Map<String, Object> andCondition) throws Exception;

    public MongoTemplate getMongoTemplate();

}
