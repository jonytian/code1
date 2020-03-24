package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 上传文件完成通知
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "1206_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_1206_MessageBody extends MessageBody {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE_ID = "1206";

    @JsonProperty("messageSeq")
    private int messageSeq;

    @JsonProperty("result")
    private int result;

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
