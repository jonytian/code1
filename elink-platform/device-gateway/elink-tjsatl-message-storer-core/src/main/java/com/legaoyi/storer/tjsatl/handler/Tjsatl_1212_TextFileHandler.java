package com.legaoyi.storer.tjsatl.handler;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.handler.MessageHandler;

/**
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1212_tjsatl_textFile" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Tjsatl_1212_TextFileHandler extends MessageHandler {

    @Autowired
    public Tjsatl_1212_TextFileHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1212_tjsatl_videoConver" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        int fileType = (Integer) messageBody.get("fileType");
        if (fileType == 0x03 || fileType == 0x04) {// 文件或者其他
            // 文件类型,0x00：图片; 0x01：音频 ;0x02：视频; 0x03：文本; 0x04：其它
            // 多媒体类型,0：音视频，1：音频，2：视频，3：视频音频；4：照片;5：文本附件；6：其他
            messageBody.put("mediaType", fileType + 2);
            // String srcFile = (String) messageBody.get("fileName");
        }
        this.getSuccessor().handle(message);
    }
}
