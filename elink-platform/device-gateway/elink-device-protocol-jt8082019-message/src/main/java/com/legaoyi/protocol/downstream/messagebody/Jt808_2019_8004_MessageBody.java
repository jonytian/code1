package com.legaoyi.protocol.downstream.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 查询服务器时间应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8004_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8004_MessageBody extends MessageBody {

    private static final long serialVersionUID = 5127626035972934474L;

    public static final String MESSAGE_ID = "8004";

    /** 服务器时间 **/
    @JsonProperty("type")
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
