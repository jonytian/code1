package com.legaoyi.management.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Entity(name = "deviceDataLimitAlarm")
@Table(name = "device_data_limit_alarm")
@XmlRootElement
public class DeviceDataLimitAlarm extends BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "deviceDataLimitAlarm";

    public static final Short STATE_DONE = 1;

    /** ip地址 **/
    @Column(name = "ip")
    private String ip;

    /** 告警类型，1:流量超额;2:数量超额;3:协议网关终端连接数超额 **/
    @Column(name = "type")
    private Short type;

    /** 状态：0：未处理；1：已处理 **/
    @Column(name = "state")
    private Short state;

    /** 网关id **/
    @Column(name = "gateway_id")
    private String gatewayId;

    /** 告警时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "alarm_time")
    private Date alarmTime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

}
