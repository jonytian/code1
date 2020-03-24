package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 驾驶员身份信息采集上报
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0702_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0702_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7700806649269928953L;

    public static final String MESSAGE_ID = "0702";

    /** ic卡状态 **/
    @JsonProperty("icCardState")
    private int icCardState;

    /** ic卡插卡/拔卡时间 **/
    @JsonProperty("icCardOptTime")
    private String icCardOptTime;

    /** ic卡读取结果 **/
    @JsonProperty("icCardReadResult")
    private int icCardReadResult;

    /** 驾驶员姓名 **/
    @JsonProperty("driverName")
    private String driverName;

    /** 从业资格证编号 **/
    @JsonProperty("qualification")
    private String qualification;

    /** 发证机构名称 **/
    @JsonProperty("certifyauth")
    private String certifyauth;

    /** 证件有效期 **/
    @JsonProperty("certValidDate")
    private String certValidDate;

    /** 驾驶员身份证 **/
    @JsonProperty("idCard")
    private String idCard;

    public final int getIcCardState() {
        return icCardState;
    }

    public final void setIcCardState(int icCardState) {
        this.icCardState = icCardState;
    }

    public final String getIcCardOptTime() {
        return icCardOptTime;
    }

    public final void setIcCardOptTime(String icCardOptTime) {
        this.icCardOptTime = icCardOptTime;
    }

    public final int getIcCardReadResult() {
        return icCardReadResult;
    }

    public final void setIcCardReadResult(int icCardReadResult) {
        this.icCardReadResult = icCardReadResult;
    }

    public final String getDriverName() {
        return driverName;
    }

    public final void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public final String getQualification() {
        return qualification;
    }

    public final void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public final String getCertifyauth() {
        return certifyauth;
    }

    public final void setCertifyauth(String certifyauth) {
        this.certifyauth = certifyauth;
    }

    public final String getCertValidDate() {
        return certValidDate;
    }

    public final void setCertValidDate(String certValidDate) {
        this.certValidDate = certValidDate;
    }

    public final String getIdCard() {
        return idCard;
    }

    public final void setIdCard(String idCard) {
        this.idCard = idCard;
    }

}
