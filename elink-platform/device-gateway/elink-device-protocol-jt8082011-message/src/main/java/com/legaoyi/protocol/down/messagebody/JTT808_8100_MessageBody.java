package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 终端注册应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8100_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8100_MessageBody extends MessageBody {

    private static final long serialVersionUID = 2071983091291547019L;

    public static final String MESSAGE_ID = "8100";

    /** 鉴权码 **/
    @JsonProperty("authCode")
    private String authCode;

    /** 应答流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 鉴权结果 **/
    @JsonProperty("result")
    private int result;

    public final String getAuthCode() {
        return authCode;
    }

    public final void setAuthCode(String authCode) {
        this.authCode = authCode;
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
