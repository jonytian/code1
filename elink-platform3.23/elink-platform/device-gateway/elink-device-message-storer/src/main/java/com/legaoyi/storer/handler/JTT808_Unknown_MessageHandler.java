package com.legaoyi.storer.handler;

import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.util.Constants;

/**
 * 协议网关对消息头已解析完成，但消息体是错误消息或者是扩展消息。开发者可以在这里扩展解析消息体
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "unknown" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_Unknown_MessageHandler extends MessageHandler {

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        // Map<?, ?> map = (Map<?, ?>) message.getMessage();
        // Map<String, Object> messageBodyMap = (Map<String, Object>)
        // map.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);
        // //正在的messageId
        // String messageId = (String)messageBodyMap.get(Constants.MAP_KEY_MESSAGE_ID);
        // //消息体，十六进制
        // String messageBody =(String) messageBodyMap.get(Constants.MAP_KEY_MESSAGE_MESSAGE_BODY);
    }

}
