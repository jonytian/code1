package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.down.messagebody.JTT808_8701_2013_MessageBody;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置初始里程
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_C4H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_C4H_2013_MessageBody extends JTT808_8701_2013_MessageBody {

    private static final long serialVersionUID = 1009597180746858610L;

    /** 记录仪实时时间，如2014-10-25 18:00:85 **/
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

    public final double getInitialMileage() {
        return initialMileage;
    }

    public final void setInitialMileage(double initialMileage) {
        this.initialMileage = initialMileage;
    }

    public final double getMileage() {
        return mileage;
    }

    public final void setMileage(double mileage) {
        this.mileage = mileage;
    }

}
