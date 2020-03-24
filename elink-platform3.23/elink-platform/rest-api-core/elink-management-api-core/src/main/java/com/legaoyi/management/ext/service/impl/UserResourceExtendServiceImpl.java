package com.legaoyi.management.ext.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.UserResource;

/**
 * @author gaoshengbo
 */
@Service("userResourceExtendService")
public class UserResourceExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return UserResource.ENTITY_NAME;
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form)
            throws Exception {
        String sql =
                "select a.id as id,c.name as categoryName,b.name as resourceName from security_user_resource a LEFT JOIN system_resource b  ON a.resource_id = b.id LEFT JOIN system_resource_category c  ON b.category_id=c.id where a.enterprise_id=? and a.role_id=?";
        String enterpriseId = (String) form.get("enterpriseId.eq");
        String roleId = (String) form.get("roleId.eq");
        String categoryId = (String) form.get("categoryId.eq");
        if (!StringUtils.isBlank(categoryId)) {
            sql = sql.concat(" and b.category_id = '" + categoryId + "'");
        }
        if (countable) {
            return this.service.pageFindBySql(sql, pageSize, pageNo, enterpriseId, roleId);
        }
        return this.service.findBySqlWithPage(sql, pageSize, pageNo, enterpriseId, roleId);
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        String roleId = (String) entity.get("roleId");
        clearCache(roleId);
        return super.service.persist(getEntityName(), entity);
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        UserResource userResource = (UserResource) this.service.get(getEntityName(), id);
        clearCache(userResource.getRoleId());
        return super.service.merge(getEntityName(), id, entity);
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            UserResource userResource = (UserResource) this.service.get(getEntityName(), id);
            clearCache(userResource.getRoleId());
            this.service.delete(userResource);
        }
    }
    
    private void clearCache(String roleId) {
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_NAVIGATION_ROLE_CACHE).evict(roleId);
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_MENU_ROLE_CACHE).clear();
    }
}
