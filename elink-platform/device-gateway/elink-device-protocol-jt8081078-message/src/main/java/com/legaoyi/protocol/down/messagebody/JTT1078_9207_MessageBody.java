package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 文件上传控制
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9207_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_9207_MessageBody extends MessageBody {

    private static final long serialVersionUID = -2222441901600081391L;

    public static final String MESSAGE_ID = "9207";

    /** 应答流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 上传控制 **/
    @JsonProperty("flag")
    private int flag;

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final int getFlag() {
        return flag;
    }

    public final void setFlag(int flag) {
        this.flag = flag;
    }

}
