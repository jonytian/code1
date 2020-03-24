package com.legaoyi.management.ext.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.persistence.jpa.util.Reflect2Entity;
import com.legaoyi.platform.ext.service.DefaultExtendService;
import com.legaoyi.platform.model.Department;

/**
 * @author gaoshengbo
 */
@Service("departmentExtendService")
public class DepartmentExtendServiceImpl extends DefaultExtendService {

    @Override
    protected String getEntityName() {
        return Department.ENTITY_NAME;
    }

    @Override
    public Object persist(Map<String, Object> entity) throws Exception {
        Department department = new Department();
        Reflect2Entity.reflect(department, entity);

        // 使用前缀发组装企业id，方便管理
        String parentId = (String) entity.get("parentId");
        if (!StringUtils.isBlank(parentId)) {
            Department parentDepartment = (Department) this.service.get(getEntityName(), parentId);
            short level = parentDepartment.getLevel();
            department.setLevel((short) (level + 1));
            department.setParentId(parentId);
        } else {
            department.setLevel((short) 0);
            parentId = "";
        }

        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("level.eq", department.getLevel());
        List<?> list = this.service.find(getEntityName(), "id", true, 1, 1, condition);
        if (list != null && !list.isEmpty()) {
            Department lastDepartment = (Department) list.get(0);
            String lastDepartmentId = lastDepartment.getId();
            department.setId(String.valueOf(Long.parseLong(lastDepartmentId) + 1));
        } else {
            department.setId(parentId + Department.START_DEPARTMENT_ID);
        }

        return this.service.persist(department);
    }

    @Override
    public void delete(Object[] ids) throws Exception {
        for (Object id : ids) {
            if (this.service.countBySql("select dept_id from security_user where dept_id=?", id) > 0) {
                throw new BizProcessException("该部门已存在用户，不能删除！");
            }
        }
        this.service.delete(getEntityName(), ids);
    }
}
