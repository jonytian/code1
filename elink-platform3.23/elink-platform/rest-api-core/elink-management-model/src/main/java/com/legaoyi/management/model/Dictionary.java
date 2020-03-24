package com.legaoyi.management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.legaoyi.platform.model.BaseModel;

@Entity(name = "dictionary")
@Table(name = "system_config_dictionary")
@XmlRootElement
public class Dictionary extends BaseModel {

    private static final long serialVersionUID = 7731266222521256687L;

    public static final String ENTITY_NAME = "dictionary";

    /** 字典类型，字典类型，1：实体对象；2：对象描述；3：消息描述；4：消息样例；5：终端设备型号 **/
    @Column(name = "type")
    private Short type;

    /** 代码 **/
    @Column(name = "code")
    private String code;

    /** 内容 **/
    @Column(name = "content")
    private String content;

    @Column(name = "remark")
    private String remark;

    @Column(name = "create_user")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "modify_user")
    private String modifyUser;

    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return this.modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

}
