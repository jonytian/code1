package com.legaoyi.platform.ext.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.legaoyi.persistence.jpa.service.GeneralService;

public abstract class DefaultExtendService implements ExtendService {

    @Autowired
    @Qualifier("generalService")
    protected GeneralService service;

    protected abstract String getEntityName();

    @Override
    public Object get(Object id) throws Exception {
        return this.service.get(getEntityName(), id);
    }

    @Override
    public long count(Map<String, Object> form) throws Exception {
        return this.service.count(getEntityName(), form);
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception {
        if (countable) {
            return this.service.pageFind(getEntityName(), selects, orderBy, desc, pageSize, pageNo, form);
        }
        return this.service.find(getEntityName(), selects, orderBy, desc, pageSize, pageNo, form);
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        return this.service.persist(getEntityName(), entity);
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        return this.service.merge(getEntityName(), id, entity);
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        this.service.delete(getEntityName(), ids);
    }

}
