package com.legaoyi.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "device")
@Table(name = "device")
@XmlRootElement
public class Device extends BaseModel {

    private static final long serialVersionUID = 5300716328017161678L;

    public static final String ENTITY_NAME = "device";

    public static final Short ALARM_SETTING_ENABLED = 1;

    /** 部门id **/
    @Column(name = "dept_id")
    private String deptId;

    /** 分组id **/
    @Column(name = "group_id")
    private String groupId = "0";

    /** 设备名称（车牌号） **/
    @Column(name = "name")
    private String name;

    /** 终端类型 **/
    @Column(name = "type")
    private Integer type;

    /** 终端型号 **/
    @Column(name = "terminal_type")
    private String terminalType;

    /** 终端id **/
    @Column(name = "terminal_id")
    private String terminalId;

    /** 终端制造商id **/
    @Column(name = "manufacturer")
    private String manufacturer;

    /** sim卡号 **/
    @Column(name = "sim_code")
    private String simCode;

    /** 鉴权码 **/
    @Column(name = "auth_code")
    private String authCode;

    /** 终端状态,0:未注册；1:已注册；2:离线；3:在线;4:已注销；5：已停用 **/
    @Column(name = "state")
    private Short state;

    /** 业务状态：0：离线；1：行驶；2：停车；3：熄火；4：无信号 **/
    @Column(name = "biz_state")
    private Short bizState;

    /** 终端协议版本 **/
    @Column(name = "protocol_version")
    private String protocolVersion;

    /** 视频协议版本 **/
    @Column(name = "vedio_protocol")
    private String vedioProtocol;

    /** 视频通道 **/
    @Column(name = "vedio_channel")
    private Integer vedioChannel = 4;

    /** 终端固件版本 **/
    @Column(name = "firmware_version")
    private String firmwareVersion;

    /** 终端最后上线时间 **/
    @Column(name = "last_online_time")
    private Long lastOnlineTime;

    /** 终端最后下线时间 **/
    @Column(name = "last_offline_time")
    private Long lastOfflineTime;

    /** 终端最后一次连接的网关 **/
    @Column(name = "last_gateway_id")
    private String gatewayId;

    /** 是否设置了平台告警规则 **/
    @Column(name = "alarm_setting_enabled")
    @JsonIgnore
    private Short alarmSettingEnabled = 0;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "register_time")
    private Date registerTime;

    @Column(name = "create_user")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_time")
    private Date modifyTime;

    @Column(name = "modify_user")
    private String modifyUser;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getBizState() {
        return bizState;
    }

    public void setBizState(Short bizState) {
        this.bizState = bizState;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getVedioProtocol() {
        return vedioProtocol;
    }

    public void setVedioProtocol(String vedioProtocol) {
        this.vedioProtocol = vedioProtocol;
    }

    public Integer getVedioChannel() {
        return vedioChannel;
    }

    public void setVedioChannel(Integer vedioChannel) {
        this.vedioChannel = vedioChannel;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public Long getLastOfflineTime() {
        return lastOfflineTime;
    }

    public void setLastOfflineTime(Long lastOfflineTime) {
        this.lastOfflineTime = lastOfflineTime;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Short getAlarmSettingEnabled() {
        return alarmSettingEnabled;
    }

    public void setAlarmSettingEnabled(Short alarmSettingEnabled) {
        this.alarmSettingEnabled = alarmSettingEnabled;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
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
