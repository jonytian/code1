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
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_MessageBody extends MessageBody {

    private static final long serialVersionUID = 2725227490254082440L;

    public static final String MESSAGE_ID = "0700";

    /** 应答流水号 **/
    @JsonProperty("messageSeq")
    private int messageSeq;

    /** 命令字 **/
    @JsonProperty("commandWord")
    private String commandWord;

    // for 809部标检测
    @JsonProperty("hexBodyData")
    private String hexBodyData;

    public final int getMessageSeq() {
        return messageSeq;
    }

    public final void setMessageSeq(int messageSeq) {
        this.messageSeq = messageSeq;
    }

    public final String getCommandWord() {
        return commandWord;
    }

    public final void setCommandWord(String commandWord) {
        this.commandWord = commandWord;
    }

    public final String getHexBodyData() {
        return hexBodyData;
    }

    public final void setHexBodyData(String hexBodyData) {
        this.hexBodyData = hexBodyData;
    }

}
