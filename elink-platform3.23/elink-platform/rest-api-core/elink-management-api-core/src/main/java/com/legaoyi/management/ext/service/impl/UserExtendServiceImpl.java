package com.legaoyi.management.ext.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Constants;
import com.legaoyi.management.util.DefaultPasswordHelper;
import com.legaoyi.persistence.jpa.util.Reflect2Entity;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.User;
import com.legaoyi.platform.model.UserRoleUser;
import com.legaoyi.platform.service.UserService;

/**
 * @author gaoshengbo
 */
@Service("userExtendService")
public class UserExtendServiceImpl extends DefaultExtendService {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Override
    protected String getEntityName() {
        return User.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        User user = new User();
        Reflect2Entity.reflect(user, entity);

        if (StringUtils.isBlank(user.getAccount())) {
            throw new BizProcessException("非法参数");
        }
        if (this.userService.isExistAccount(user.getAccount())) {
            throw new BizProcessException("用户账号已存在");
        }

        DefaultPasswordHelper.encryptPassword(user);
        return this.service.persist(user);
    }

    @Override
    public Object merge(Object id, Map<String, Object> entity) throws Exception {
        User user = new User();
        Reflect2Entity.reflect(user, entity);
        User oldUser = (User) this.service.get(User.ENTITY_NAME, id);
        if (user.getAccount() != null && !user.getAccount().equals(oldUser.getAccount()) && this.userService.isExistAccount(user.getAccount())) {
            throw new BizProcessException("用户账号已存在");
        }
        if (StringUtils.isNotBlank(oldUser.getOpenId())) {
            this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(oldUser.getOpenId());
        }
        if (StringUtils.isNotBlank(oldUser.getPhone())) {
            this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(oldUser.getPhone());
        }
        this.cacheManager.getCache(Constants.CACHE_NAME_USER_INFO_CACHE).evict(id);
        this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(oldUser.getAccount());
        String password = user.getPassword();
        String oldPassword = oldUser.getPassword();
        user = oldUser;
        Reflect2Entity.reflect(user, entity);
        if (!StringUtils.isBlank(password) && !password.equals(oldPassword)) {
            DefaultPasswordHelper.encryptPassword(user);
        }
        return this.service.merge(user);
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            User user = (User) this.service.get(getEntityName(), id);
            if (StringUtils.isNotBlank(user.getOpenId())) {
                this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(user.getOpenId());
            }
            if (StringUtils.isNotBlank(user.getPhone())) {
                this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(user.getPhone());
            }
            this.cacheManager.getCache(Constants.CACHE_NAME_AAS_USER_CACHE).evict(user.getAccount());
            this.cacheManager.getCache(Constants.CACHE_NAME_USER_ROLE_CACHE).evict(user.getId());
            this.cacheManager.getCache(Constants.CACHE_NAME_USER_INFO_CACHE).evict(user.getId());
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("userId.eq", id);
            this.service.delete(UserRoleUser.ENTITY_NAME, andCondition);
            this.service.delete(user);
        }
    }

    @Override
    public Object query(String[] selects, String orderBy, boolean desc, int pageSize, int pageNo, boolean countable, Map<String, Object> form) throws Exception {
        String sql =
                "select u.id as id,u.name as name,u.account as account,u.org_id as orgId,o.name as orgName,u.dept_id as deptId,d.name as deptName,u.state as state,u.type as type from security_user u LEFT JOIN security_department d ON u.dept_id=d.id LEFT JOIN  security_organization o ON u.org_id=o.id where  u.enterprise_id=?";
        String enterpriseId = (String) form.get("enterpriseId.eq");
        String orgId = (String) form.get("orgId.eq");
        if (!StringUtils.isBlank(orgId)) {
            sql = sql.concat(" AND u.org_id = '" + orgId + "'");
        }
        String deptId = (String) form.get("deptId.eq");
        if (!StringUtils.isBlank(deptId)) {
            sql = sql.concat(" AND u.dept_id like '" + deptId + "%'");
        }
        Integer type = (Integer) form.get("type.eq");
        if (type != null) {
            sql = sql.concat(" AND u.type = " + type);
        }
        if (countable) {
            return this.service.pageFindBySql(sql, pageSize, pageNo, enterpriseId);
        }
        return this.service.findBySqlWithPage(sql, pageSize, pageNo, enterpriseId);
    }

}
