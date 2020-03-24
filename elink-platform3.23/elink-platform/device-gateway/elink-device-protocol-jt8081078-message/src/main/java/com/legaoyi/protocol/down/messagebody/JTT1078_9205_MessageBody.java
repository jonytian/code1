package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 查询资源列表
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9205_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_9205_MessageBody extends MessageBody {

    private static final long serialVersionUID = -2222441901600081391L;

    public static final String MESSAGE_ID = "9205";

    /** 逻辑通道号 **/
    @JsonProperty("channelId")
    private int channelId;

    /** 起始时间 **/
    @JsonProperty("startTime")
    private String startTime;

    /** 结束时间 **/
    @JsonProperty("endTime")
    private String endTime;

    /** 报警标志 **/
    @JsonProperty("alarmFlag")
    private String alarmFlag;

    /** 音视频资源类型 **/
    @JsonProperty("resourceType")
    private int resourceType;

    /** 码流类型 **/
    @JsonProperty("streamType")
    private int streamType;

    /** 存储器类型 **/
    @JsonProperty("storeType")
    private int storeType;

    public final int getChannelId() {
        return channelId;
    }

    public final void setChannelId(int channelId) {
        this.channelId = channelId;
    }

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

    public final String getAlarmFlag() {
        return alarmFlag;
    }

    public final void setAlarmFlag(String alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public final int getResourceType() {
        return resourceType;
    }

    public final void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public final int getStreamType() {
        return streamType;
    }

    public final void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public final int getStoreType() {
        return storeType;
    }

    public final void setStoreType(int storeType) {
        this.storeType = storeType;
    }

}
