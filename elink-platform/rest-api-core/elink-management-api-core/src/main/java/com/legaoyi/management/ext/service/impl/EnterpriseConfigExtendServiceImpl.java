package com.legaoyi.management.ext.service.impl;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Constants;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.EnterpriseConfig;

@Service("enterpriseConfigExtendService")
public class EnterpriseConfigExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return EnterpriseConfig.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        throw new BizProcessException("不支持此操作");
    }

    @Override
    @CacheEvict(value = Constants.CACHE_NAME_ENTERPRISE_CONFIG_CACHE, key = "#id")
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        return super.merge(id, entity);
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        throw new BizProcessException("不支持此操作");
    }
}
