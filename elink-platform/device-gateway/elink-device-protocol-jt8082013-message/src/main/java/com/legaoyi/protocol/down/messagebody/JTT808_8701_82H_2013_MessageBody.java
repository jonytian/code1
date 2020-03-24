package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.down.messagebody.JTT808_8701_2013_MessageBody;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置车辆信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_82H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_82H_2013_MessageBody extends JTT808_8701_2013_MessageBody {

    private static final long serialVersionUID = -6463742521217112673L;

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
