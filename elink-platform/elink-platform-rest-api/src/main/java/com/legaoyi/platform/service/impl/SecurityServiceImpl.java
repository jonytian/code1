package com.legaoyi.platform.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.service.SecurityService;

@Service("securityService")
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Override
    @Cacheable(cacheNames = Constants.CACHE_NAME_AAS_USER_CACHE, key = "#userAccount", unless = "#result == null")
    public Map<?, ?> getUserAccount(String userAccount) throws Exception {
        List<?> list = this.service.findBySql(
                "select u.id as id,name as name,org_id as orgId,dept_id as deptId,r.role_id as roleId,password as password, salt as salt,state as state,type as type,u.enterprise_id as enterpriseId from security_user u LEFT JOIN security_user_role_user r ON u.id=r.user_id where state = 1 AND account= ?  LIMIT 1",
                userAccount);
        if (list != null && !list.isEmpty()) {
            return (Map<?, ?>) list.get(0);
        }
        return null;
    }

}
