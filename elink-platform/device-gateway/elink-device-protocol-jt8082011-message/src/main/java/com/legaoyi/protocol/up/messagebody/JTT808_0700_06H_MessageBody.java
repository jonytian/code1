package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集车辆VIN号，车牌号，车牌分类
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_06H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_06H_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = 3967333775572778460L;

    /** 车辆vin号 **/
    @JsonProperty("vin")
    private String vin;

    /** 车牌号 **/
    @JsonProperty("plateNo")
    private String plateNo;

    /** 车牌分类 **/
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
