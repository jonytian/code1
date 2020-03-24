package com.legaoyi.protocol.message.decoder;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;

public interface MessageBodyDecoder {

    public static final String MESSAGE_BODY_DECODER_BEAN_PREFIX = "elink_";

    public static final String MESSAGE_BODY_DECODER_BEAN_SUFFIX = "_messageBodyDecoder";

    public MessageBody decode(byte[] bytes) throws IllegalMessageException;
}
