package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 采集车辆信息(05H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_05H_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0700_05H_MessageBody extends Jt808_2019_0700_MessageBody {

    private static final long serialVersionUID = -5838552668759472320L;

    /** 车辆识别代号 **/
    @JsonProperty("vin")
    private String vin;

    /** 车牌号 **/
    @JsonProperty("plateNo")
    private String plateNo;

    /** 机动车号牌分类 **/
    @JsonProperty("plateType")
    private String plateType;

    public final String getVin() {
        return vin;
    }

    public final void setVin(String vin) {
        this.vin = vin;
    }

    public final String getPlateNo() {
        return plateNo;
    }

    public final void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public final String getPlateType() {
        return plateType;
    }

    public final void setPlateType(String plateType) {
        this.plateType = plateType;
    }

}
