package com.legaoyi.client.up.messagebody;

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
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0102_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0102_2011_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6313237473030187434L;

    public static final String JTT808_MESSAGE_ID = "0102";

    /** 鉴权码 **/
    @JsonProperty("authCode")
    private String authCode;

    public final String getAuthCode() {
        return authCode;
    }

    public final void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

}
