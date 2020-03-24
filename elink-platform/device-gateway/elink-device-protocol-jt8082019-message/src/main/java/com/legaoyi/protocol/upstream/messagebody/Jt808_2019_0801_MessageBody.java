package com.legaoyi.protocol.upstream.messagebody;

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
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0801_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0801_MessageBody extends Jt808_2019_0800_MessageBody {

    private static final long serialVersionUID = 4171381179742082258L;

    public static final String MESSAGE_ID = "0801";

    /** 位置信息 **/
    @JsonProperty("gpsInfo")
    private MessageBody gpsInfo;

    /** 文件路径 **/
    @JsonProperty("filePath")
    private String filePath;

    /** 多媒体数据 **/
    @JsonIgnore
    private byte[] fileData;

    public final MessageBody getGpsInfo() {
        return gpsInfo;
    }

    public final void setGpsInfo(MessageBody gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

    public final String getFilePath() {
        return filePath;
    }

    public final void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public final byte[] getFileData() {
        return fileData;
    }

    public final void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

}
