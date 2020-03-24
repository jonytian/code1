package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集最近360h内累计行驶里程数据
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_03H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_03H_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = -6363626242087597596L;

    /** 时间 **/
    @JsonProperty("realTime")
    private String realTime;

    /** 累计里程 **/
    @JsonProperty("mileage")
    private double mileage;

    public final String getRealTime() {
        return realTime;
    }

    public final void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public final double getMileage() {
        return mileage;
    }

    public final void setMileage(double mileage) {
        this.mileage = mileage;
    }

}
