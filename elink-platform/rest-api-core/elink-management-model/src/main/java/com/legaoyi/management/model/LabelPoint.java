package com.legaoyi.management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "labelPoint")
@Table(name = "lbs_label_point")
@XmlRootElement
public class LabelPoint extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = -3563575406550821868L;

    public static final String ENTITY_NAME = "labelPoint";

    /** 标注点名称 **/
    @Column(name = "name")
    private String name;

    /** 标注类型：1、点；2、圆形区域；3、矩形区域；4、多边形;5、路线 **/
    @Column(name = "type")
    private Short type;

    /** 业务类型；1、停车场；2、途径点；3、站点；4、区域查车；5、电子围栏；6、其他待定 **/
    @Column(name = "biz_type")
    private Short bizType;

    /** 设置详情（json格式） **/
    @Column(name = "setting")
    private String setting;

    /** 备注 **/
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getBizType() {
        return bizType;
    }

    public void setBizType(Short bizType) {
        this.bizType = bizType;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

}
