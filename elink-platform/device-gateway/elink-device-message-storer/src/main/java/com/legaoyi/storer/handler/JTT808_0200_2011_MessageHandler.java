package com.legaoyi.storer.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.service.ConfigService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0200_2011_MessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("configService")
    private ConfigService configService;

    @Autowired
    public JTT808_0200_2011_MessageHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0200_2011_messageCacheHandler") MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        // 消息处理链入口
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);

        // 由于位置信息数据量巨大，使用内置_id作为主键以提高性能,额外增加id做为主键查询
        if (messageBody.get("id") == null) {
            messageBody.put("id", IdGenerator.nextIdStr());
        }

        Long gpsTime = (Long) messageBody.get("gpsTime");
        Long time = message.getCreateTime();
        // gps时间超前1小时或者滞后俩小时,纠正为服务器时间
        if (gpsTime > time + 1 * 60 * 60 * 1000 || gpsTime + 2 * 60 * 60 * 1000 < time) {
            messageBody.put("gpsTime", time);
        }

        Map<?, ?> enterpriseConfig = (Map<?, ?>) message.getExtAttribute("enterpriseConfig");
        if (enterpriseConfig == null) {
            Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
            enterpriseConfig = this.configService.getEnterpriseConfig((String) device.get(Constants.MAP_KEY_ENTERPRISE_ID));
            message.putExtAttribute("enterpriseConfig", enterpriseConfig);
        }

        getSuccessor().handle(message);
    }

}
