package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0101_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0101_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6485608570477022496L;

    public static final String MESSAGE_ID = "0101";

    /** 应答消息id **/
    @JsonProperty("messageId")
    private String messageId;

    /** 应答消息流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 结果 **/
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
