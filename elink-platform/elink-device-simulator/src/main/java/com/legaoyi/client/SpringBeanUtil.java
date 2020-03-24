package com.legaoyi.client;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.legaoyi.client.message.builder.MessageBuilder;
import com.legaoyi.client.message.handler.MessageHandler;
import com.legaoyi.client.util.Constants;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.decoder.MessageBodyDecoder;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.ServerRuntimeContext;

public class SpringBeanUtil {

    public static MessageHandler getMessageHandler(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageHandler) ServerRuntimeContext.getBean(Constants.MESSAGE_HANDLER_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(Constants.MESSAGE_HANDLER_BEAN_SUFFIX));
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }
    
    public static MessageBuilder getMessageBuilder(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageBuilder) ServerRuntimeContext.getBean(Constants.MESSAGE_BUILDER_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(Constants.MESSAGE_BUILDER_BEAN_SUFFIX));
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }

    public static MessageBody getMessageBody(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageBody) ServerRuntimeContext.getBean(MessageBody.MESSAGE_BODY_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(MessageBody.MESSAGE_BODY_BEAN_SUFFIX));
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }

    public static MessageBodyEncoder getMessageBodyEncoder(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageBodyEncoder) ServerRuntimeContext.getBean(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(MessageBodyEncoder.MESSAGE_BODY_ENCODER_BEAN_SUFFIX));
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }

    public static MessageBodyDecoder getMessageBodyDecoder(String messageId, String version) throws IllegalMessageException {
        try {
            return (MessageBodyDecoder) ServerRuntimeContext.getBean(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_PREFIX.concat(messageId).concat("_").concat(version).concat(MessageBodyDecoder.MESSAGE_BODY_DECODER_BEAN_SUFFIX));
        } catch (NoSuchBeanDefinitionException e) {
        }
        throw new IllegalMessageException();
    }
}
