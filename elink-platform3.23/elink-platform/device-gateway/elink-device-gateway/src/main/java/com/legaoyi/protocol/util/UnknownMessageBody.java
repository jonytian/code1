package com.legaoyi.protocol.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

public class UnknownMessageBody extends MessageBody {

    private static final long serialVersionUID = -6014899754094914113L;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("messageBody")
    private String messageBody;

    public final String getMessageId() {
        return messageId;
    }

    public final void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public final String getMessageBody() {
        return messageBody;
    }

    public final void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
