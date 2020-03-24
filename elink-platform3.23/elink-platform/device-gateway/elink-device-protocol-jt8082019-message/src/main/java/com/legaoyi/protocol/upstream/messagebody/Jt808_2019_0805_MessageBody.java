package com.legaoyi.protocol.upstream.messagebody;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 摄像头立即拍摄命令应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0805_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0805_MessageBody extends MessageBody {

    private static final long serialVersionUID = 2682604275836450908L;

    public static final String MESSAGE_ID = "0805";

    /** 应答流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 结果 **/
    @JsonProperty("result")
    private int result;

    /** 多媒体id列表 **/
    @JsonProperty("mediaDataIdList")
    private List<Long> mediaDataIdList;

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final int getResult() {
        return result;
    }

    public final void setResult(int result) {
        this.result = result;
    }

    public final List<Long> getMediaDataIdList() {
        return mediaDataIdList;
    }

    public final void setMediaDataIdList(List<Long> mediaDataIdList) {
        this.mediaDataIdList = mediaDataIdList;
    }

}
