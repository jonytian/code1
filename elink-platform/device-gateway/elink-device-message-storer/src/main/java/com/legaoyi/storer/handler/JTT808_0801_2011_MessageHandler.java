package com.legaoyi.storer.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.Constants;

/**
 * 多媒体数据上传
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0801_2011_MessageHandler extends MessageHandler {

    @Autowired
    public JTT808_0801_2011_MessageHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011_mediaConver" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        getSuccessor().handle(message);
    }

}
