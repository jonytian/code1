package com.legaoyi.management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "deviceAlarmSetting")
@Table(name = "device_alarm_setting")
@XmlRootElement
public class DeviceAlarmSetting extends com.legaoyi.platform.model.BaseModel {

    private static final long serialVersionUID = -3563575406550821868L;

    public static final String ENTITY_NAME = "deviceAlarmSetting";

    /** 设备id **/
    @Column(name = "device_id")
    private String deviceId;

    /** 规则名称 **/
    @Column(name = "name")
    private String name;

    /** 规则类型：业务类型：1：平台围栏；2：终端围栏 **/
    @Column(name = "biz_type")
    private Short bizType = 1;

    /** 规则类型：业务id **/
    @Column(name = "biz_id")
    private String bizId;

    /** 规则类型：1:原地设防；2：围栏报警；3：超速报警 **/
    @Column(name = "type")
    private Short type;

    /** 状态：0：禁用；1：启用 **/
    @Column(name = "state")
    private Short state = 1;

    /** 告警规则详情，json格式 **/
    @Column(name = "setting")
    private String setting;

    /** 开始有效时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    /** 有效结束时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_user")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "modify_user")
    private String modifyUser;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getBizType() {
        return bizType;
    }

    public void setBizType(Short bizType) {
        this.bizType = bizType;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
