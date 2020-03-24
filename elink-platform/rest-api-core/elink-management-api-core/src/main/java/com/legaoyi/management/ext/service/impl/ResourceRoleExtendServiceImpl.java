package com.legaoyi.management.ext.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import com.legaoyi.common.util.Constants;
import com.legaoyi.management.model.ResourceRole;
import com.legaoyi.platform.ext.service.DefaultExtendService;

/**
 * @author gaoshengbo
 */
@Service("resourceRoleExtendService")
public class ResourceRoleExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return ResourceRole.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        clearCache();
        return super.persist(entity);
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        clearCache();
        return super.merge(id, entity);

    }

    @Override
    public void delete(Object[] ids) throws Exception {
        String sql = "DELETE FROM security_user_resource WHERE enterprise_id IN ( SELECT a.enterprise_id FROM system_role_enterprise a, system_role_resource b WHERE a.role_id = b.role_id AND b.id = ? ) AND resource_id = ?";
        for (Object id : ids) {
            ResourceRole resourceRole = (ResourceRole) this.service.get(getEntityName(), id);
            this.service.deleteBySql(sql, resourceRole.getId(), resourceRole.getResourceId());
            this.service.delete(resourceRole);
        }
        clearCache();
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception {
        String conSql = "";
        String roleId = (String) form.get("roleId.eq");
        if (StringUtils.isNotBlank(roleId)) {
            conSql = " and a.role_id = '" + roleId + "'";
        }

        String categoryId = (String) form.get("categoryId.eq");
        if (StringUtils.isNotBlank(categoryId)) {
            conSql += " and c.category_id = '" + categoryId + "'";
        }

        String sql = "select a.id as id,a.role_id as roleId,a.resource_id as resourceId,b.name as roleName,c.name as resourceName,d.name as categoryName" + "  from system_role_resource a,system_role b,system_resource c,system_resource_category d "
                + " where a.role_id=b.id and a.resource_id=c.id and d.id= c.category_id " + conSql + " order by a.role_id,c.category_id";
        if (countable) {
            return this.service.pageFindBySql(sql, pageSize, pageNo);
        }
        return this.service.findBySql(sql, pageSize, pageNo);
    }

    private void clearCache() {
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_MENU_CACHE).clear();
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_NAVIGATION_CACHE).clear();
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_MENU_ROLE_CACHE).clear();
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_NAVIGATION_ROLE_CACHE).clear();
    }
}
