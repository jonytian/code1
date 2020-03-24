package com.legaoyi.protocol.up.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 采集最近360小时内的行驶速度数据
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_05H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_05H_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = -5838552668759472320L;

    /** 速度开始时间 **/
    @JsonProperty("startTime")
    private String startTime;

    /** 速度列表 **/
    @JsonProperty("speedList")
    private List<Integer> speedList;

    public final String getStartTime() {
        return startTime;
    }

    public final void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public final List<Integer> getSpeedList() {
        return speedList;
    }

    public final void setSpeedList(List<Integer> speedList) {
        this.speedList = speedList;
    }

}
