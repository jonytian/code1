package com.legaoyi.management.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.management.service.EnterpriseService;
import com.legaoyi.management.util.DefaultPasswordHelper;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.persistence.jpa.util.Reflect2Entity;
import com.legaoyi.platform.model.Enterprise;
import com.legaoyi.platform.model.User;
import com.legaoyi.platform.service.UserService;

@Service("enterpriseService")
@Transactional
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Override
    public User register(Map<String, Object> entity) throws Exception {
        Enterprise enterprise = new Enterprise();
        Reflect2Entity.reflect(enterprise, entity);
        enterprise.setState(Enterprise.STATE_ENABLE);

        User user = new User();
        Reflect2Entity.reflect(user, entity);
        user.setState(User.STATE_ENABLE);
        user.setName(user.getAccount());

        if (StringUtils.isBlank(user.getAccount())) {
            throw new BizProcessException("非法参数");
        }
        if (this.userService.isExistAccount(user.getAccount())) {
            throw new BizProcessException("该用户账号已存在");
        }
        enterprise.setId(null);
        DefaultPasswordHelper.encryptPassword(user);
        enterprise = (Enterprise) this.service.persist(enterprise);
        user.setEnterpriseId(enterprise.getId());
        return (User) this.service.persist(user);
    }

}
