package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 事件报告
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0301_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0301_MessageBody extends MessageBody {

    private static final long serialVersionUID = 4938629352784311011L;

    public static final String MESSAGE_ID = "0301";

    /** 事件id **/
    @JsonProperty("eventId")
    private int eventId;

    public final int getEventId() {
        return eventId;
    }

    public final void setEventId(int eventId) {
        this.eventId = eventId;
    }

}
