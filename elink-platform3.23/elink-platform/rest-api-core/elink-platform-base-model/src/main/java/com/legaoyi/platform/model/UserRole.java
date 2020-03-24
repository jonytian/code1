package com.legaoyi.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "userRole")
@Table(name = "security_user_role")
@XmlRootElement
public class UserRole extends BaseModel {

    private static final long serialVersionUID = 7731266222521256687L;

    public static final String ENTITY_NAME = "userRole";

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "remark")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
