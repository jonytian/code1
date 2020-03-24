package com.legaoyi.management.ext.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.User;
import com.legaoyi.platform.model.UserRoleUser;

/**
 * @author gaoshengbo
 */
@Service("userRoleUserExtendService")
public class UserRoleUserExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Override
    protected String getEntityName() {
        return UserRoleUser.ENTITY_NAME;
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception {
        String sql =
                "select a.id as id,a.role_id as roleId,a.user_id as userId,b.name as roleName,c.name as userName from security_user_role_user a,security_user_role b,security_user c where a.role_id=b.id and a.user_id=c.id and a.enterprise_id=? and a.role_id=?";
        String enterpriseId = (String) form.get("enterpriseId.eq");
        String roleId = (String) form.get("roleId.eq");
        if (countable) {
            return this.service.pageFindBySql(sql, pageSize, pageNo, enterpriseId, roleId);
        }
        return this.service.findBySqlWithPage(sql, pageSize, pageNo, enterpriseId, roleId);
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        UserRoleUser userRoleUser = (UserRoleUser) super.service.persist(getEntityName(), entity);
        this.clearCahce(userRoleUser.getUserId());
        return userRoleUser;
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        UserRoleUser userRoleUser = (UserRoleUser) super.service.merge(getEntityName(), id, entity);
        this.clearCahce(userRoleUser.getUserId());
        return userRoleUser;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            UserRoleUser userRoleUser = (UserRoleUser) this.get(id);
            clearCahce(userRoleUser.getUserId());
            this.service.delete(userRoleUser);
        }
    }

    private void clearCahce(String userId) throws Exception {
        User user = (User) this.service.get(User.ENTITY_NAME, userId);
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(user.getAccount());
        this.cacheManager.getCache(Constants.CACHE_NAME_USER_ROLE_CACHE).evict(userId);
    }
}
