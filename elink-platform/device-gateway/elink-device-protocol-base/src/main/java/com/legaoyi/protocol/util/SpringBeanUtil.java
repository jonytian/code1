package com.legaoyi.protocol.util;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;

public class SpringBeanUtil {

    public static MessageBody getMessageBody(String messageId, String version) throws IllegalMessageException {
        try {
            return ServerRuntimeContext.getBean(MessageBody.MESSAGE_BODY_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(MessageBody.MESSAGE_BODY_BEAN_SUFFIX), MessageBody.class);
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }

    public static MessageBodyEncoder getMessageBodyEncoder(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageBodyEncoder) ServerRuntimeContext.getBean(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX),
                    MessageBodyEncoder.class);
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }

    public static MessageBodyDecoder getMessageBodyDecoder(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageBodyDecoder) ServerRuntimeContext.getBean(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX),
                    MessageBodyDecoder.class);
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }
}
