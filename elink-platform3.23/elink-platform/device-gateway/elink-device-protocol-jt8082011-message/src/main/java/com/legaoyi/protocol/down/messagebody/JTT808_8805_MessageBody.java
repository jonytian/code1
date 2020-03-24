package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 单条存储多媒体数据检索上传命令
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8805_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8805_MessageBody extends MessageBody {

    private static final long serialVersionUID = -8795529504235113960L;

    public static final String MESSAGE_ID = "8805";

    /** 多媒体id **/
    @JsonProperty("mediaDataId")
    private int mediaDataId;

    /** 删除标识 **/
    @JsonProperty("saveFlag")
    private int saveFlag;

    public final int getMediaDataId() {
        return mediaDataId;
    }

    public final void setMediaDataId(int mediaDataId) {
        this.mediaDataId = mediaDataId;
    }

    public final int getSaveFlag() {
        return saveFlag;
    }

    public final void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

}
