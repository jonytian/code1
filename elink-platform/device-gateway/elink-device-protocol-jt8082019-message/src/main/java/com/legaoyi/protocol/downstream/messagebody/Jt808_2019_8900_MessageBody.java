package com.legaoyi.protocol.downstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 数据下行透传
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8900_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8900_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7117409715902609098L;

    public static final String MESSAGE_ID = "8900";

    /** 透传消息类型 **/
    @JsonProperty("messageType")
    private String messageType;

    /** 透传消息内容 **/
    @JsonProperty("message")
    private String message;

    public final String getMessageType() {
        return messageType;
    }

    public final void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
    }

}
