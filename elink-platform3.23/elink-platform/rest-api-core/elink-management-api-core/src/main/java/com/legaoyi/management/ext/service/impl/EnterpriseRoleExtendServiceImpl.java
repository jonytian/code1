package com.legaoyi.management.ext.service.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Constants;
import com.legaoyi.management.model.EnterpriseRole;
import com.legaoyi.platform.ext.service.DefaultExtendService;

/**
 * @author gaoshengbo
 */
@Service("enterpriseRoleExtendService")
public class EnterpriseRoleExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return EnterpriseRole.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        String enterpriseId = (String) entity.get("enterpriseId");
        Map<String, Object> andCondition = Maps.newHashMap();
        andCondition.put("enterpriseId.eq", enterpriseId);
        if (this.service.count(EnterpriseRole.ENTITY_NAME, andCondition) > 0) {
            throw new BizProcessException("一个企业只能分配一个角色！");
        }
        EnterpriseRole enterpriseRole = (EnterpriseRole) super.persist(entity);
        clearCache(enterpriseRole.getEnterpriseId());
        return enterpriseRole;
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        EnterpriseRole enterpriseRole = (EnterpriseRole) super.merge(id, entity);
        clearCache(enterpriseRole.getEnterpriseId());
        return enterpriseRole;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        // 删除已分配给用户的菜单
        String sql = "DELETE FROM security_user_resource WHERE resource_id IN ( SELECT b.resource_id FROM system_role_enterprise a, system_role_resource b WHERE a.role_id = b.role_id AND a.id = ? ) AND enterprise_id = ?";
        for (Object id : ids) {
            EnterpriseRole enterpriseRole = (EnterpriseRole) this.service.get(getEntityName(), id);
            this.service.deleteBySql(sql, enterpriseRole.getId(), enterpriseRole.getEnterpriseId());
            clearCache(enterpriseRole.getEnterpriseId());
            this.service.delete(enterpriseRole);
        }
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception {
        String sql =
                " SELECT a.id as id, a.role_id as roleId,a.enterprise_id as enterpriseId,b.name as roleName,c.name as enterpriseName from system_role_enterprise a LEFT JOIN system_role b ON a.role_id = b.id LEFT JOIN security_enterprise c on a.enterprise_id = c.id ORDER BY enterprise_id";
        return this.service.findBySqlWithPage(sql, pageSize, pageNo, new Object[] {});
    }

    private void clearCache(String enterpriseId) {
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_MENU_CACHE).clear();
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_MENU_ROLE_CACHE).clear();
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_NAVIGATION_CACHE).evict(enterpriseId);
    }

}
