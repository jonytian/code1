package com.legaoyi.protocol.message.encoder;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;

public interface MessageBodyEncoder {

    public static final String MESSAGE_BODY_ENCODER_BEAN_PREFIX = "elink_";

    public static final String MESSAGE_BODY_ENCODER_BEAN_SUFFIX = "_messageBodyEncoder";

    public byte[] encode(MessageBody message) throws IllegalMessageException;
}
