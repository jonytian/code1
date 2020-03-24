package com.legaoyi.management.ext.service.impl;

import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Organization;

/**
 * @author gaoshengbo
 */
@Service("organizationExtendService")
public class OrganizationExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return Organization.ENTITY_NAME;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            if (this.service.countBySql("select org_id from security_department where org_id=?", id) > 0) {
                throw new BizProcessException("该组织机构已存在部门，不能删除！");
            }
        }
        this.service.delete(getEntityName(), ids);
    }
}
