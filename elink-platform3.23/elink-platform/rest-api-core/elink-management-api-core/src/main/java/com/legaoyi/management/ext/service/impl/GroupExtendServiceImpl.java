package com.legaoyi.management.ext.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.management.model.Group;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Device;

/**
 * @author gaoshengbo
 */
@Service("groupExtendService")
public class GroupExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return Group.ENTITY_NAME;
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        Map<String, Object> andCondition = new HashMap<String, Object>();
        for (Object id : ids) {
            andCondition.put("parentId.eq", id);
            if (this.service.count(getEntityName(), andCondition) > 0) {
                throw new BizProcessException("父分组不能删除");
            }
            andCondition.clear();
            andCondition.put("groupId.eq", id);
            if (this.service.count(Device.ENTITY_NAME, andCondition) > 0) {
                throw new BizProcessException("分组下存在设备，不能删除");
            }
        }
        this.service.delete(getEntityName(), ids);
    }

}
