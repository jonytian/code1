package com.legaoyi.platform.ext.service;

import java.util.Map;

/**
 * @author gaoshengbo
 */
public interface ExtendService {

    /**
     * 获取记录
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public Object get(Object id) throws Exception;

    /**
     * 统计记录条数
     * 
     * @param form
     * @return
     * @throws Exception
     */
    public long count(Map<String, Object> form) throws Exception;

    /**
     * 查询记录列表
     * 
     * @param selects
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param countable
     * @param form
     * @return
     * @throws Exception
     */
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception;

    /**
     * 添加记录
     * 
     * @param entity
     * @return
     * @throws Exception
     */
    public Object persist(Map<String, Object> entity) throws Exception;

    /**
     * 修改记录
     * 
     * @param id
     * @param entity
     * @return
     * @throws Exception
     */
    public Object merge(Object id, Map<String, Object> entity) throws Exception;

    /**
     * 删除记录
     * 
     * @param ids
     * @throws Exception
     */
    public void delete(Object[] ids) throws Exception;

}
