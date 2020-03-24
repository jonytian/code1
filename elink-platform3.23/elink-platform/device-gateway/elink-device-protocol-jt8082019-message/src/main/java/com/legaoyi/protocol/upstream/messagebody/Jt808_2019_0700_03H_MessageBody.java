package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 采集累计行驶里程(03H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_03H_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0700_03H_MessageBody extends Jt808_2019_0700_MessageBody {

    private static final long serialVersionUID = -6363626242087597596L;

    /** 记录仪实时时间 **/
    @JsonProperty("realTime")
    private String realTime;

    /** 记录仪初次安装时间 **/
    @JsonProperty("installTime")
    private String installTime;

    /** 初始里程 **/
    @JsonProperty("initialMileage")
    private double initialMileage;

    /** 累计行驶里程 **/
    @JsonProperty("mileage")
    private double mileage;

    public final String getRealTime() {
        return realTime;
    }

    public final void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    public final String getInstallTime() {
        return installTime;
    }

    public final void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public final double getMileage() {
        return mileage;
    }

    public final void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public final double getInitialMileage() {
        return initialMileage;
    }

    public final void setInitialMileage(double initialMileage) {
        this.initialMileage = initialMileage;
    }

}
