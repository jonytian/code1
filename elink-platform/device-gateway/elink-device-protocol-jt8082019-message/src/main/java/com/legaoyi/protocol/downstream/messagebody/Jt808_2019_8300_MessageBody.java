package com.legaoyi.protocol.downstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 文本信息下发
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8300_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8300_MessageBody extends MessageBody {

    private static final long serialVersionUID = -634570788090610592L;

    public static final String MESSAGE_ID = "8300";

    /** 标识，二进制字符串 **/
    @JsonProperty("flag")
    private String flag;

    /** 类型 **/
    @JsonProperty("flag")
    private int type;

    /** 文本信息 **/
    @JsonProperty("text")
    private String text;

    public final String getFlag() {
        return flag;
    }

    public final void setFlag(String flag) {
        this.flag = flag;
    }

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final String getText() {
        return text;
    }

    public final void setText(String text) {
        this.text = text;
    }

}
