package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 上传终端音视频属性
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "1005_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_1005_MessageBody extends MessageBody {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE_ID = "1005";

    /** 起始时间 **/
    @JsonProperty("startTime")
    private String startTime;

    /** 结束时间 **/
    @JsonProperty("endTime")
    private String endTime;

    /** 上车人数 **/
    @JsonProperty("inCar")
    private int inCar;

    /** 下车人数 **/
    @JsonProperty("offCar")
    private int offCar;

    public final String getStartTime() {
        return startTime;
    }

    public final void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public final String getEndTime() {
        return endTime;
    }

    public final void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public final int getInCar() {
        return inCar;
    }

    public final void setInCar(int inCar) {
        this.inCar = inCar;
    }

    public final int getOffCar() {
        return offCar;
    }

    public final void setOffCar(int offCar) {
        this.offCar = offCar;
    }

}
