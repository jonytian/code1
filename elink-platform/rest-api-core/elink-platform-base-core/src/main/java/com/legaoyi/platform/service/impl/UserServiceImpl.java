package com.legaoyi.platform.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.model.User;
import com.legaoyi.platform.service.UserService;

/**
 * @author gaoshengbo
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Override
    public boolean isExistAccount(String userAccount) throws Exception {
        return this.service.countBySql("select 1 from security_user where account=?", userAccount) > 0;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_USER_INFO_CACHE, key = "#userId", unless = "#result == null")
    public User findUserById(String userId) throws Exception {
        return (User) this.service.get(User.ENTITY_NAME, userId);
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_USER_ROLE_CACHE, key = "#userId", unless = "#result == null")
    public List<?> findUserRoleByUser(String userId) throws Exception {
        return this.service.findBySql("select a.id,a.code, a.name from security_user_role a, security_user_role_user b where a.id = b.role_id and b.user_id = ?", userId);
    }
}
