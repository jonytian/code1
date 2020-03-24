package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 云台变倍控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9306_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_9306_MessageBody extends MessageBody {

    private static final long serialVersionUID = -2222441901600081391L;

    public static final String MESSAGE_ID = "9306";

    /** 逻辑通道号 **/
    @JsonProperty("channelId")
    private int channelId;

    /** 变倍控制 **/
    @JsonProperty("type")
    private int type;

    public final int getChannelId() {
        return channelId;
    }

    public final void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

}
