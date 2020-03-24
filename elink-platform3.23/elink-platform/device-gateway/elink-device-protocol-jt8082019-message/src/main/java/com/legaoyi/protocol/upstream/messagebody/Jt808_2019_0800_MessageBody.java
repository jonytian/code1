package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 多媒体信息事件上传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0800_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0800_MessageBody extends MessageBody {

    private static final long serialVersionUID = -8430545859213912804L;

    public static final String MESSAGE_ID = "0800";

    /** 多媒体数据id **/
    @JsonProperty("mediaDataId")
    private long mediaDataId;

    /** 多媒体类型 **/
    @JsonProperty("mediaType")
    private int mediaType;

    /** 多媒体格式编码 **/
    @JsonProperty("mediaFormatCode")
    private int mediaFormatCode;

    /** 事件项编码 **/
    @JsonProperty("eventCode")
    private int eventCode;

    /** 通道id **/
    @JsonProperty("channelId")
    private int channelId;

    public final long getMediaDataId() {
        return mediaDataId;
    }

    public final void setMediaDataId(long mediaDataId) {
        this.mediaDataId = mediaDataId;
    }

    public final int getMediaType() {
        return mediaType;
    }

    public final void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public final int getMediaFormatCode() {
        return mediaFormatCode;
    }

    public final void setMediaFormatCode(int mediaFormatCode) {
        this.mediaFormatCode = mediaFormatCode;
    }

    public final int getEventCode() {
        return eventCode;
    }

    public final void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public final int getChannelId() {
        return channelId;
    }

    public final void setChannelId(int channelId) {
        this.channelId = channelId;
    }

}
