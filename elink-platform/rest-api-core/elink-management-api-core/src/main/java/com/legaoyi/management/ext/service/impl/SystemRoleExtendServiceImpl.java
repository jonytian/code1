package com.legaoyi.management.ext.service.impl;

import org.springframework.stereotype.Service;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.management.model.SystemRole;
import com.legaoyi.platform.ext.service.DefaultExtendService;

/**
 * @author gaoshengbo
 */
@Service("systemRoleExtendService")
public class SystemRoleExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return SystemRole.ENTITY_NAME;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            if (this.service.countBySql("select 1 from system_role_enterprise where role_id = ?", id) > 0) {
                throw new BizProcessException("权限已分配不能删除！");
            }
            this.service.delete(getEntityName(), id);
        }
    }

}
