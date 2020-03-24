package com.legaoyi.storer.tjsatl.handler;

import java.io.File;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.persistence.redis.service.RedisService;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.Constants;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1212_tjsatl" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class Tjsatl_1212_MessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("redisService")
    private RedisService<?> redisService;

    @Autowired
    public Tjsatl_1212_MessageHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "1212_tjsatl_textFile" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

        // 文件名称命名规则为：<文件类型>_<通道号>_<报警类型>_<序号>_<报警编号>.<后缀名>
        String fileName = (String) messageBody.get("fileName");
        messageBody.put("filePath", fileName);

        int index = fileName.lastIndexOf(File.separator);
        fileName = fileName.substring(index);
        String[] arr = fileName.split("_");
        if (arr.length > 1) {
            messageBody.put("channelId", Integer.parseInt(arr[1]));
            messageBody.put("eventCode", Integer.parseInt(arr[2]));
            messageBody.put("bizId", arr[4].split("[.]")[0]);
        }
        // 苏标
        messageBody.put("bizType", 3);

        // 文件类型,0x00：图片; 0x01：音频 ;0x02：视频; 0x03：文本; 0x04：其它
        int fileType = (Integer) messageBody.get("fileType");
        // 0：图像；1：音频；2：视频
        messageBody.put("mediaType", fileType);

        redisService.decr(message.getGatewayId());

        this.getSuccessor().handle(message);
    }
}
