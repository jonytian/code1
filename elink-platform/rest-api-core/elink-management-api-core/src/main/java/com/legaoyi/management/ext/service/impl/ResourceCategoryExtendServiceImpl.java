package com.legaoyi.management.ext.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Constants;
import com.legaoyi.management.model.ResourceCategory;
import com.legaoyi.platform.ext.service.DefaultExtendService;

/**
 * @author gaoshengbo
 */
@Service("resourceCategoryExtendService")
public class ResourceCategoryExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return ResourceCategory.ENTITY_NAME;
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_MENU_CACHE).clear();
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_NAVIGATION_CACHE).clear();
        return super.merge(id, entity);

    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            if (this.service.countBySql("select 1 from system_resource_category where parent_id = ?", id) > 0) {
                throw new BizProcessException("存在下级菜单不能删除！");
            }
            if (this.service.countBySql("select 1 from system_resource where category_id = ?", id) > 0) {
                throw new BizProcessException("存在下级菜单不能删除！");
            }
            this.service.delete(getEntityName(), id);
        }
    }

}
