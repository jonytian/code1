package com.legaoyi.protocol.up.messagebody;

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
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0201_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0201_MessageBody extends MessageBody {

    private static final long serialVersionUID = 1258632212412669618L;

    public static final String MESSAGE_ID = "0201";

    /** 应答流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 位置信息 **/
    @JsonProperty("gpsInfo")
    private JTT808_0200_MessageBody gpsInfo;

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final JTT808_0200_MessageBody getGpsInfo() {
        return gpsInfo;
    }

    public final void setGpsInfo(JTT808_0200_MessageBody gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

}
