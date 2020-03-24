package com.legaoyi.protocol.upstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 终端通用应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-04-15
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0001_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_0001_MessageBody extends MessageBody {

    private static final long serialVersionUID = -4514267627327653857L;

    public static final String MESSAGE_ID = "0001";

    /** 应答对应的平台消息id **/
    @JsonProperty("messageId")
    private String messageId;

    /** 平台消息的流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 终端处理结果 **/
    @JsonProperty("result")
    private int result;

    public final String getMessageId() {
        return messageId;
    }

    public final void setMessageId(String messageId) {
        this.messageId = messageId;
    }

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

}
