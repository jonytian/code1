package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 信息服务
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8304_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8304_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3441467355858813877L;

    public static final String MESSAGE_ID = "8304";

    /** 信息类型 **/
    @JsonProperty("type")
    private int type;

    /** 信息内容 **/
    @JsonProperty("info")
    private String info;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final String getInfo() {
        return info;
    }

    public final void setInfo(String info) {
        this.info = info;
    }

}
