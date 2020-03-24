package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 临时位置跟踪控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8202_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8202_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3225041292826124334L;

    public static final String MESSAGE_ID = "8202";

    /** 时间间隔 **/
    @JsonProperty("interval")
    private int interval;

    /** 位置跟踪有效期 **/
    @JsonProperty("expTime")
    private long expTime;

    public final int getInterval() {
        return interval;
    }

    public final void setInterval(int interval) {
        this.interval = interval;
    }

    public final long getExpTime() {
        return expTime;
    }

    public final void setExpTime(long expTime) {
        this.expTime = expTime;
    }

}
