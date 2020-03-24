package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 采集行驶记录仪车辆特征系数应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_04H_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0700_04H_MessageBody extends Jt808_2019_0700_MessageBody {

    private static final long serialVersionUID = 8562390939836556284L;

    /** 记录仪当前时间 **/
    @JsonProperty("realTime")
    private String realTime;

    /** 记录仪脉冲系数 **/
    @JsonProperty("coefficient")
    private int coefficient;

    public final String getRealTime() {
        return realTime;
    }

    public final void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public final int getCoefficient() {
        return coefficient;
    }

    public final void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

}
