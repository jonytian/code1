package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 多媒体数据上传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0801_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0801_MessageBody extends JTT808_0800_MessageBody {

    private static final long serialVersionUID = 4171381179742082258L;

    public static final String MESSAGE_ID = "0801";

    /** 位置信息 **/
    @JsonProperty("gpsInfo")
    private MessageBody gpsInfo;

    /** 多媒体数据 **/
    @JsonIgnore
    private byte[] mediaData;

    /** 多媒体文件存储路径 **/
    @JsonProperty("filePath")
    private String filePath;

    public final MessageBody getGpsInfo() {
        return gpsInfo;
    }

    public final void setGpsInfo(MessageBody gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

    public final byte[] getMediaData() {
        return mediaData;
    }

    public final void setMediaData(byte[] mediaData) {
        this.mediaData = mediaData;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
