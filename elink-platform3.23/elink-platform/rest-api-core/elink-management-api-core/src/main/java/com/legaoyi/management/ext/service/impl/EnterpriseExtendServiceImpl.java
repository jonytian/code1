package com.legaoyi.management.ext.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.persistence.jpa.util.Reflect2Entity;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Enterprise;
import com.legaoyi.platform.model.User;

@Service("enterpriseExtendService")
public class EnterpriseExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return Enterprise.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        Enterprise enterprise = new Enterprise();
        Reflect2Entity.reflect(enterprise, entity);

        enterprise.setState(Enterprise.STATE_ENABLE);
        String enterpriseId = (String) entity.get("enterpriseId");
        Enterprise optEnterprise = (Enterprise) this.service.get(Enterprise.ENTITY_NAME, enterpriseId);
        short level = optEnterprise.getLevel();
        enterprise.setLevel((short) (level + 1));

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("level.eq", enterprise.getLevel());
        condition.put("parentId.eq", enterpriseId);
        List<?> list = this.service.find(Enterprise.ENTITY_NAME, "id", true, 1, 1, condition);
        if (list != null && !list.isEmpty()) {
            Enterprise lastEnterprise = (Enterprise) list.get(0);
            String lastEnterpriseId = lastEnterprise.getId();
            enterprise.setId(String.valueOf(Long.parseLong(lastEnterpriseId) + 1));
        } else {
            enterprise.setId(enterpriseId + Enterprise.START_ENTITY_ID);
        }

        enterprise.setParentId(enterpriseId);
        enterprise = (Enterprise) this.service.persist(enterprise);
        return enterprise;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            Map<String, Object> andCondition = new HashMap<String, Object>();
            andCondition.put("enterpriseId.eq", id);
            if (this.service.count(User.ENTITY_NAME, andCondition) > 0) {
                throw new BizProcessException("该公司已在使用中，不能删除！");
            }
        }
        super.delete(ids);
    }
}
