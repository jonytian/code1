package com.legaoyi.persistence.jpa.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.persistence.jpa.dao.GeneralDao;
import com.legaoyi.persistence.jpa.model.Pagination;
import com.legaoyi.persistence.jpa.service.GeneralService;

/**
 * @author gaoshengbo
 */
@Service("generalService")
@Transactional
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    @Qualifier("generalDao")
    private GeneralDao generalDao;

    @Override
    public Object get(String entityName, Object id) throws Exception {
        return this.generalDao.get(entityName, id);
    }

    @Override
    public long count(String entityName, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.count(entityName, andCondition);
    }

    @Override
    public long countByHql(String hql, Object... params) throws Exception {
        return this.generalDao.countByHql(hql, params);
    }

    @Override
    public long countBySql(String sql, Object... params) throws Exception {
        return this.generalDao.countBySql(sql, params);
    }

    @Override
    public List<?> findBySql(String sql) throws Exception {
        return this.generalDao.findBySql(sql);
    }

    @Override
    public List<?> findBySql(String sql, Object... params) throws Exception {
        return this.generalDao.findBySql(sql, params);
    }

    @Override
    public List<?> findBySqlWithPage(String sql, int pageSize, int pageNo, Object... params) throws Exception {
        return this.generalDao.findBySqlWithPage(sql, pageSize, pageNo, params);
    }

    @Override
    public Pagination pageFindBySql(String sql, int pageSize, int pageNo, Object... params) throws Exception {
        return this.generalDao.pageFindBySql(sql, pageSize, pageNo, params);
    }

    @Override
    public List<?> findByHql(String hql) throws Exception {
        return this.generalDao.findByHql(hql);
    }

    @Override
    public List<?> findByHql(String hql, Object... params) throws Exception {
        return this.generalDao.findByHql(hql, params);
    }

    @Override
    public Pagination pageFindByHql(String hql, int pageSize, int pageNo, Object... params) throws Exception {
        return this.generalDao.pageFindByHql(hql, pageSize, pageNo, params);
    }

    @Override
    public List<?> findAll(String entityName) throws Exception {
        return this.generalDao.findAll(entityName);
    }

    @Override
    public List<?> findAll(String entityName, String[] selectFields) throws Exception {
        return this.generalDao.findAll(entityName, selectFields);
    }

    @Override
    public List<?> find(String entityName, String orderBy, boolean desc, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.find(entityName, orderBy, desc, andCondition);
    }

    @Override
    public List<?> find(String entityName, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.find(entityName, orderBy, desc, pageSize, pageNo, andCondition);
    }

    @Override
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.find(entityName, selectFields, orderBy, desc, andCondition);
    }

    @Override
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.find(entityName, selectFields, orderBy, desc, pageSize, pageNo, andCondition);
    }

    @Override
    public Pagination pageFind(String entityName, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.pageFind(entityName, orderBy, desc, pageSize, pageNo, andCondition);
    }

    @Override
    public Pagination pageFind(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        return this.generalDao.pageFind(entityName, selectFields, orderBy, desc, pageSize, pageNo, andCondition);
    }

    @Override
    public Object persist(String entityName, Map<String, Object> entity) throws Exception {
        return this.generalDao.persist(entityName, entity);
    }

    @Override
    public Object persist(Object entity) throws Exception {
        return this.generalDao.persist(entity);
    }

    @Override
    public Object merge(String entityName, Object id, Map<String, Object> entity) throws Exception {
        return this.generalDao.merge(entityName, id, entity);
    }

    @Override
    public Object merge(Object entity) throws Exception {
        return this.generalDao.merge(entity);
    }

    @Override
    public void delete(String entityName, Object id) throws Exception {
        this.generalDao.delete(entityName, id);
    }

    @Override
    public void delete(String entityName, Map<String, Object> andCondition) throws Exception {
        this.generalDao.delete(entityName, andCondition);
    }

    @Override
    public void delete(Object entity) throws Exception {
        this.generalDao.delete(entity);
    }

    @Override
    public void delete(String entityName, Object[] ids) throws Exception {
        if (ids != null) {
            for (Object id : ids) {
                this.delete(entityName, id);
            }
        }
    }

    @Override
    public void deleteBySql(String sql, Object... params) throws Exception {
        this.generalDao.deleteBySql(sql, params);
    }

    @Override
    public boolean isExistField(String entityName, String fieldName) {
        return this.generalDao.isExistField(entityName, fieldName);
    }
}
