package com.legaoyi.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.legaoyi.platform.model.BaseModel;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-03-14
 */
@Entity(name = "enterpriseRole")
@Table(name = "system_role_enterprise")
@XmlRootElement
public class EnterpriseRole extends BaseModel {

    private static final long serialVersionUID = -2685755584170050608L;

    public static final String ENTITY_NAME = "enterpriseRole";

    @Column(name = "role_id")
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
