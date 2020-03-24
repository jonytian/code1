package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 音视频回放控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9202_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_9202_MessageBody extends MessageBody {

    private static final long serialVersionUID = -2222441901600081391L;

    public static final String MESSAGE_ID = "9202";

    /** 逻辑通道号 **/
    @JsonProperty("channelId")
    private int channelId;

    /** 回放方式 **/
    @JsonProperty("playbackType")
    private int playbackType;

    /** 快进或快退倍数 **/
    @JsonProperty("playTimes")
    private int playTimes;

    /** 回放起始时间 **/
    @JsonProperty("startTime")
    private String startTime;

    public final int getChannelId() {
        return channelId;
    }

    public final void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public final int getPlaybackType() {
        return playbackType;
    }

    public final void setPlaybackType(int playbackType) {
        this.playbackType = playbackType;
    }

    public final int getPlayTimes() {
        return playTimes;
    }

    public final void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public final String getStartTime() {
        return startTime;
    }

    public final void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
