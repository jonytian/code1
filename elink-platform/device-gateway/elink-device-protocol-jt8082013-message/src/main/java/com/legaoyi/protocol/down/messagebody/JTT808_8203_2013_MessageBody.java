package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 人工确认报警信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8203_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8203_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = -3225041292826124334L;

    public static final String MESSAGE_ID = "8203";

    /** 报警消息流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 人工确认报警类型 **/
    @JsonProperty("alarmType")
    private int alarmType;

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final int getAlarmType() {
        return alarmType;
    }

    public final void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

}
