package com.legaoyi.management.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.UserRole;

/**
 * @author gaoshengbo
 */
@Service("userRoleExtendService")
public class UserRoleExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return UserRole.ENTITY_NAME;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            if (this.service.countBySql("select role_id from security_user_role_user where role_id=?", id) > 0) {
                throw new BizProcessException("该角色下已分配用户，不能删除！");
            }
        }
        this.service.delete(getEntityName(), ids);
    }
}
