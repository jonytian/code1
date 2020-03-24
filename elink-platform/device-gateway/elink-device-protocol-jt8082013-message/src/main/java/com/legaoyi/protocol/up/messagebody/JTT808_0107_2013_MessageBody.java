package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 查询终端属性应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0107_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0107_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3392914164582453750L;

    public static final String MESSAGE_ID = "0107";

    /** 终端类型 **/
    @JsonProperty("terminalType")
    private String terminalType;

    /** 制造商id **/
    @JsonProperty("mfrsId")
    private String mfrsId;

    /** 终端型号 **/
    @JsonProperty("terminalModel")
    private String terminalModel;

    /** 终端id **/
    @JsonProperty("terminalId")
    private String terminalId;

    /** 终端sim卡iccid **/
    @JsonProperty("iccId")
    private String iccId;

    /** 终端硬件版本号 **/
    @JsonProperty("hardwareVersion")
    private String hardwareVersion;

    /** 终端固件版本号 **/
    @JsonProperty("firmwareVersion")
    private String firmwareVersion;

    /** gnss模块属性 **/
    @JsonProperty("gnssAttribute")
    private String gnssAttribute;

    /** 通讯模块属性 **/
    @JsonProperty("cmAttribute")
    private String cmAttribute;

    public final String getTerminalType() {
        return terminalType;
    }

    public final void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public final String getMfrsId() {
        return mfrsId;
    }

    public final void setMfrsId(String mfrsId) {
        this.mfrsId = mfrsId;
    }

    public final String getTerminalModel() {
        return terminalModel;
    }

    public final void setTerminalModel(String terminalModel) {
        this.terminalModel = terminalModel;
    }

    public final String getTerminalId() {
        return terminalId;
    }

    public final void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public final String getIccId() {
        return iccId;
    }

    public final void setIccId(String iccId) {
        this.iccId = iccId;
    }

    public final String getHardwareVersion() {
        return hardwareVersion;
    }

    public final void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public final String getFirmwareVersion() {
        return firmwareVersion;
    }

    public final void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public final String getGnssAttribute() {
        return gnssAttribute;
    }

    public final void setGnssAttribute(String gnssAttribute) {
        this.gnssAttribute = gnssAttribute;
    }

    public final String getCmAttribute() {
        return cmAttribute;
    }

    public final void setCmAttribute(String cmAttribute) {
        this.cmAttribute = cmAttribute;
    }

}
