package com.legaoyi.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "userRoleUser")
@Table(name = "security_user_role_user")
@XmlRootElement
public class UserRoleUser extends BaseModel {

    private static final long serialVersionUID = 7709929607156330466L;

    public static final String ENTITY_NAME = "userRoleUser";

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "user_id")
    private String userId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
