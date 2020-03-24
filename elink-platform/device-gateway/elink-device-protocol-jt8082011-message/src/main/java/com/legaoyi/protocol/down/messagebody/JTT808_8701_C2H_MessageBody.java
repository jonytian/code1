package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置记录仪时钟
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_C2H_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_C2H_MessageBody extends JTT808_8701_MessageBody {

    private static final long serialVersionUID = 1009597180746858610L;

    /** 记录仪时钟，如2014-10-25 18:00:85 **/
    @JsonProperty("realTime")
    private String realTime;

    public final String getRealTime() {
        return realTime;
    }

    public final void setRealTime(String realTime) {
        this.realTime = realTime;
    }

}
