package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 实时音视频传输控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9102_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_9102_MessageBody extends MessageBody {

    private static final long serialVersionUID = -2222441901600081391L;

    public static final String MESSAGE_ID = "9102";

    /** 逻辑通道号 **/
    @JsonProperty("channelId")
    private int channelId;

    /** 控制指令 **/
    @JsonProperty("command")
    private int command;

    /** 关闭音视频类型 **/
    @JsonProperty("commadType")
    private int commadType;

    /** 码流类型 **/
    @JsonProperty("streamType")
    private int streamType;

    public final int getChannelId() {
        return channelId;
    }

    public final void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public final int getCommand() {
        return command;
    }

    public final void setCommand(int command) {
        this.command = command;
    }

    public final int getCommadType() {
        return commadType;
    }

    public final void setCommadType(int commadType) {
        this.commadType = commadType;
    }

    public final int getStreamType() {
        return streamType;
    }

    public final void setStreamType(int streamType) {
        this.streamType = streamType;
    }

}
