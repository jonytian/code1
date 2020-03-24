package com.legaoyi.platform.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "enterpriseConfig")
@Table(name = "security_enterprise")
@XmlRootElement
public class EnterpriseConfig implements Serializable {

    private static final long serialVersionUID = -5171820598348224401L;

    public static final String ENTITY_NAME = "enterpriseConfig";

    @Id
    @Column(name = "id", length = 32)
    private String id;

    /** 告警规则配置，json格式 **/
    @Column(name = "alarm_setting")
    private String alarmSetting;

    /** 允许接入设备数量 **/
    @Column(name = "device_limit")
    private Integer deviceLimit;

    /** 上行消息频率控制，每10分钟或每小时，逗号分隔 **/
    @Column(name = "message_num_limit")
    private String messageNumLimit;

    /** 流量控制，每10分钟或每小时，逗号分隔 **/
    @Column(name = "message_byte_limit")
    private String messageByteLimit;

    /** 允许的上行的消息 **/
    @Column(name = "upstream_message_limit")
    private String upstreamMessageLimit;

    /*** 允许下行的消息 **/
    @Column(name = "downstream_message_limit")
    private String downstreamMessageLimit;

    public  String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public  String getAlarmSetting() {
        return alarmSetting;
    }

    public  void setAlarmSetting(String alarmSetting) {
        this.alarmSetting = alarmSetting;
    }

    public  Integer getDeviceLimit() {
        return deviceLimit;
    }

    public  void setDeviceLimit(Integer deviceLimit) {
        this.deviceLimit = deviceLimit;
    }

    public  String getMessageNumLimit() {
        return messageNumLimit;
    }

    public  void setMessageNumLimit(String messageNumLimit) {
        this.messageNumLimit = messageNumLimit;
    }

    public  String getMessageByteLimit() {
        return messageByteLimit;
    }

    public  void setMessageByteLimit(String messageByteLimit) {
        this.messageByteLimit = messageByteLimit;
    }

    public  String getUpstreamMessageLimit() {
        return upstreamMessageLimit;
    }

    public  void setUpstreamMessageLimit(String upstreamMessageLimit) {
        this.upstreamMessageLimit = upstreamMessageLimit;
    }

    public  String getDownstreamMessageLimit() {
        return downstreamMessageLimit;
    }

    public  void setDownstreamMessageLimit(String downstreamMessageLimit) {
        this.downstreamMessageLimit = downstreamMessageLimit;
    }

}
