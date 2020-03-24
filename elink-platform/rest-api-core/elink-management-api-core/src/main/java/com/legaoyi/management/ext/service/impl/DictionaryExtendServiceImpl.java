package com.legaoyi.management.ext.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.legaoyi.common.util.Constants;
import com.legaoyi.management.model.Dictionary;
import com.legaoyi.platform.ext.service.DefaultExtendService;

/**
 * @author gaoshengbo
 */
@Service("dictionaryExtendService")
public class DictionaryExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return Dictionary.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        Dictionary dictionary = (Dictionary) super.persist(entity);
        this.cacheManager.getCache(Constants.CACHE_NAME_DICTIONARY_CACHE).evict(dictionary.getCode());
        return dictionary;
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        Dictionary dictionary = (Dictionary) super.merge(id, entity);
        this.cacheManager.getCache(Constants.CACHE_NAME_DICTIONARY_CACHE).evict(dictionary.getCode());
        return dictionary;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            Dictionary dictionary = (Dictionary) this.service.get(getEntityName(), id);
            this.cacheManager.getCache(Constants.CACHE_NAME_DICTIONARY_CACHE).evict(dictionary.getCode());
            this.service.delete(dictionary);
        }
    }

}
