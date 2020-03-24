package com.legaoyi.storer.handler;

import java.util.Map;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.ServerRuntimeContext;
import com.legaoyi.common.message.ExchangeMessage;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0001_2011" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0001_2011_MessageHandler extends MessageHandler {

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<?, ?> messageBody = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        String respMessageId = (String) messageBody.get(Constants.MAP_KEY_MESSAGE_ID);
        try {
            MessageHandler messageHandler = ServerRuntimeContext.getBean(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(respMessageId).concat("_response").concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX), MessageHandler.class);
            messageHandler.handle(message);
        } catch (NoSuchBeanDefinitionException e) {

        }
    }
}
