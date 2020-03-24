package com.legaoyi.persistence.jpa.service;

import java.util.List;
import java.util.Map;

import com.legaoyi.persistence.jpa.model.Pagination;

/**
 * @author gaoshengbo
 */
public interface GeneralService {

    /**
     * 根据id获取对象数据详情
     * 
     * @param entityName 实体对象名
     * @param 记录id
     * @return
     * @throws Exception
     */
    public Object get(String entityName, Object id) throws Exception;

    /**
     * 根据实体对象名获取所有数据明细列表，注意数据量比较大时不要调用该接口
     * 
     * @param entityName 实体对象名
     * @return
     * @throws Exception
     */
    public List<?> findAll(String entityName) throws Exception;

    /**
     * 根据实体对象名获取所有数据列表，返回指定的字段信息
     * 
     * @param entityName 实体对象名
     * @param selectFields 需要返回信息的字段名
     * @return
     * @throws Exception
     */
    public List<?> findAll(String entityName, String[] selectFields) throws Exception;

    /**
     * 根据实体对象名统计满足条件的记录条数
     * 
     * @param entityName 实体对象名
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public long count(String entityName, Map<String, Object> andCondition) throws Exception;

    /**
     * 根据给定的hql语句统计满足条件的记录条数（注：hql语句为普通的查询语句，中不需要带count函数）
     * 
     * @param hql hql语句，可带参数"?"
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public long countByHql(String hql, Object... params) throws Exception;

    /**
     * 根据给定的sql语句统计满足条件的记录条数（注：sql语句为普通的查询语句，中不需要带count函数）
     * 
     * @param sql sql语句，可带参数"?"
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public long countBySql(String sql, Object... params) throws Exception;

    /**
     * 根据给定的sql语句返回查询结果列表
     * 
     * @param sql sql语句
     * @return
     * @throws Exception
     */
    public List<?> findBySql(String sql) throws Exception;

    /**
     * 根据给定的sql语句返回满足条件的记录列表
     * 
     * @param sql sql语句，可带参数"?"
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public List<?> findBySql(String sql, Object... params) throws Exception;

    /**
     * 根据给定的sql语句返回满足条件的分页结果列表
     * 
     * @param sql sql语句，可带参数"?"
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public List<?> findBySqlWithPage(String sql, int pageSize, int pageNo, Object... params) throws Exception;

    /**
     * 根据给定的sql语句返回满足条件的分页结果列表
     * 
     * @param sql sql语句，可带参数"?"
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public Pagination pageFindBySql(String sql, int pageSize, int pageNo, Object... params) throws Exception;

    /**
     * 根据给定的hql语句返回查询结果列表
     * 
     * @param hql hql语句
     * @return
     * @throws Exception
     */
    public List<?> findByHql(String hql) throws Exception;

    /**
     * 根据给定的hql语句返回满足条件的记录列表
     * 
     * @param hql hql语句，可带参数"?"
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public List<?> findByHql(String hql, Object... params) throws Exception;

    /**
     * 根据给定的hql语句返回满足条件的分页结果列表
     * 
     * @param hql hql语句，可带参数"?"
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param params 数组类型，"?"参数对应的值
     * @return
     * @throws Exception
     */
    public Pagination pageFindByHql(String hql, int pageSize, int pageNo, Object... params) throws Exception;

    /**
     * 根据实体对象名返回满足条件的对象数据列表
     * 
     * @param entityName 实体对象名
     * @param orderBy 排序字段名
     * @param desc 是否降序
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String orderBy, boolean desc, Map<String, Object> andCondition) throws Exception;

    /**
     * 根据实体对象名返回满足条件的对象数据分页结果列表
     * 
     * @param entityName 实体对象名
     * @param orderBy 排序字段名
     * @param desc 是否降序
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 根据实体对象名查询满足条件的数据列表，返回指定的字段信息
     * 
     * @param entityName 实体对象名
     * @param selectFields 需要返回信息的字段名
     * @param orderBy 排序字段名
     * @param desc 是否降序
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, Map<String, Object> andCondition) throws Exception;

    /**
     * 根据实体对象名查询满足条件的数据列表，返回指定的字段信息
     * 
     * @param entityName 实体对象名
     * @param selectFields 需要返回信息的字段名
     * @param orderBy 排序字段名
     * @param desc 是否降序
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 根据实体对象名查询满足条件的分页数据列表
     * 
     * @param entityName 实体对象名
     * @param orderBy 排序字段名
     * @param desc 是否降序
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public Pagination pageFind(String entityName, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 根据实体对象名查询满足条件的分页数据列表，返回指定的字段信息
     * 
     * @param entityName 实体对象名
     * @param selectFields 需要返回信息的字段名
     * @param desc 是否降序
     * @param pageSize 每页大小
     * @param pageNo 当前页码
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @return
     * @throws Exception
     */
    public Pagination pageFind(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception;

    /**
     * 新增实体对象数据
     * 
     * @param entityName 实体对象名
     * @param entity key-val形式，key为对象字段名
     * @return
     * @throws Exception
     */
    public Object persist(String entityName, Map<String, Object> entity) throws Exception;

    /**
     * 新增实体对象数据
     * 
     * @param entity 实体对象
     * @return
     * @throws Exception
     */
    public Object persist(Object entity) throws Exception;

    /**
     * 更新实体对象数据
     * 
     * @param entityName 实体对象名
     * @param id 记录id
     * @param entity key-val形式，key为对象字段名
     * @return
     * @throws Exception
     */
    public Object merge(String entityName, Object id, Map<String, Object> entity) throws Exception;

    /**
     * 更新实体对象数据
     * 
     * @param entity detached状态的实体对象
     * @return
     * @throws Exception
     */
    public Object merge(Object entity) throws Exception;

    /**
     * 根据记录id删除实体对象数据
     * 
     * @param entityName 实体对象名
     * @param id 记录id
     * @throws Exception
     */
    public void delete(String entityName, Object id) throws Exception;

    /**
     * 根据记录id列表删除实体对象数据
     * 
     * @param entityName 实体对象名
     * @param ids 记录id列表
     * @throws Exception
     */
    public void delete(String entityName, Object[] ids) throws Exception;

    /**
     * 根据条件删除实体对象数据
     * 
     * @param entityName 实体对象名
     * @param andCondition 查询条件，支持gt、gte、lt、lte、eq、neq、like操作，key-val形式,其中 key格式: {fieldName}.gt
     * @throws Exception
     */
    public void delete(String entityName, Map<String, Object> andCondition) throws Exception;

    /**
     * 删除实体对象数据
     * 
     * @param entity managed状态的实体对象
     * @throws Exception
     */
    public void delete(Object entity) throws Exception;

    /**
     * 根据给定的sql语句删除数据
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBySql(String sql, Object... params) throws Exception;

    /**
     * 检测fieldName是否是entityName实体的属性
     * 
     * @param entityName
     * @param fieldName
     * @return
     */
    public boolean isExistField(String entityName, String fieldName);

}
