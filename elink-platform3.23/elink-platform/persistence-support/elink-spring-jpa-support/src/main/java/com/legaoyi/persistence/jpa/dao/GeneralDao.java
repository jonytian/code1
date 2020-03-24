package com.legaoyi.persistence.jpa.dao;

import java.util.List;
import java.util.Map;

import com.legaoyi.persistence.jpa.model.Pagination;

/**
 * @author gaoshengbo
 */
public interface GeneralDao {

    /**
     * 根据实体名称以及主键值获取记录
     * 
     * @param entityName
     * @param id
     * @return
     * @throws Exception
     */
    public Object get(String entityName, Object id) throws Exception;

    /**
     * 根据实体名称以及条件返回记录数
     * 
     * @param entityName
     * @param andCondition
     * @return
     * @throws Exception
     */
    public long count(String entityName, Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param hql
     * @param params
     * @return
     * @throws Exception
     */
    public long countByHql(String hql, Object... params) throws Exception;

    /**
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public long countBySql(String sql, Object... params) throws Exception;

    /**
     * 
     * @param entityName
     * @return
     * @throws Exception
     */
    public List<?> findAll(String entityName) throws Exception;

    /**
     * 
     * @param entityName
     * @param selectFields
     * @return
     * @throws Exception
     */
    public List<?> findAll(String entityName, String[] selectFields) throws Exception;

    /**
     * 
     * @param sql
     * @return
     * @throws Exception
     */
    public List<?> findBySql(String sql) throws Exception;

    /**
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public List<?> findBySql(String sql, Object... params) throws Exception;

    
    /**
     * 
     * @param sql
     * @param pageSize
     * @param pageNo
     * @param params
     * @return
     * @throws Exception
     */
    public List<?> findBySqlWithPage(String sql, int pageSize, int pageNo, Object... params) throws Exception;
    
    /**
     * 
     * @param sql
     * @param pageSize
     * @param pageNo
     * @param params
     * @return
     * @throws Exception
     */
    public Pagination pageFindBySql(String sql, int pageSize, int pageNo, Object... params) throws Exception;

    /**
     * 
     * @param hql
     * @return
     * @throws Exception
     */
    public List<?> findByHql(String hql) throws Exception;

    /**
     * 
     * @param hql
     * @param params
     * @return
     * @throws Exception
     */
    public List<?> findByHql(String hql, Object... params) throws Exception;

    /**
     * 
     * @param hql
     * @param pageSize
     * @param pageNo
     * @param params
     * @return
     * @throws Exception
     */
    public Pagination pageFindByHql(String hql, int pageSize, int pageNo, Object... params) throws Exception;

    /**
     * 
     * @param entityName
     * @param orderBy
     * @param desc
     * @param andCondition
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String orderBy, boolean desc, Map<String, Object> andCondition)
            throws Exception;

    /**
     * 
     * @param entityName
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param andCondition
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String orderBy, boolean desc, int pageSize, int pageNo,
            Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param entityName
     * @param selectFields
     * @param orderBy
     * @param desc
     * @param andCondition
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc,
            Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param entityName
     * @param selectFields
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param andCondition
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize,
            int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param entityName
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param andCondition
     * @return
     * @throws Exception
     */
    public Pagination pageFind(String entityName, String orderBy, boolean desc, int pageSize, int pageNo,
            Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param entityName
     * @param selectFields
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param andCondition
     * @return
     * @throws Exception
     */
    public Pagination pageFind(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize,
            int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 
     * @param entityName
     * @param entity
     * @return
     * @throws Exception
     */
    public Object persist(String entityName, Map<String, Object> entity) throws Exception;

    /**
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    public Object persist(Object entity) throws Exception;

    /**
     * 
     * @param entityName
     * @param id
     * @param entity
     * @return
     * @throws Exception
     */
    public Object merge(String entityName, Object id, Map<String, Object> entity) throws Exception;

    /**
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    public Object merge(Object entity) throws Exception;

    /**
     * 
     * @param entity
     * @throws Exception
     */
    public void delete(Object entity) throws Exception;

    /**
     * 
     * @param entityName
     * @param id
     * @throws Exception
     */
    public void delete(String entityName, Object id) throws Exception;

    /**
     * 
     * @param entityName
     * @param andCondition
     * @throws Exception
     */
    public void delete(String entityName, Map<String, Object> andCondition) throws Exception;
    
    /**
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBySql(String sql, Object... params) throws Exception;
    
    /**
     * 
     * @param entityName
     * @param fieldName
     * @return
     */
    public boolean isExistField(String entityName, String fieldName);

}
