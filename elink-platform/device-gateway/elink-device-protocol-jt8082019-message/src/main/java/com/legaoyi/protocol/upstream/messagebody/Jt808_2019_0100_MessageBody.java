package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 终端注册
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0100_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0100_MessageBody extends MessageBody {

    private static final long serialVersionUID = -526107277912187732L;

    public static final String MESSAGE_ID = "0100";

    /** 省域id **/
    @JsonProperty("provinceId")
    private int provinceId;

    /** 市县id **/
    @JsonProperty("cityId")
    private int cityId;

    /**
     * 制造商id
     */
    @JsonProperty("mfrsId")
    private String mfrsId;

    /** 终端型号 **/
    @JsonProperty("terminalType")
    private String terminalType;

    /** 终端id **/
    @JsonProperty("terminalId")
    private String terminalId;

    /** 车牌颜色 **/
    @JsonProperty("vinColor")
    private int vinColor;

    /** 车辆标识 **/
    @JsonProperty("vin")
    private String vin;

    public final int getProvinceId() {
        return provinceId;
    }

    public final void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public final int getCityId() {
        return cityId;
    }

    public final void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public final String getMfrsId() {
        return mfrsId;
    }

    public final void setMfrsId(String mfrsId) {
        this.mfrsId = mfrsId;
    }

    public final String getTerminalType() {
        return terminalType;
    }

    public final void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public final String getTerminalId() {
        return terminalId;
    }

    public final void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public final int getVinColor() {
        return vinColor;
    }

    public final void setVinColor(int vinColor) {
        this.vinColor = vinColor;
    }

    public final String getVin() {
        return vin;
    }

    public final void setVin(String vin) {
        this.vin = vin;
    }

}
