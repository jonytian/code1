package com.legaoyi.storer.handler;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.util.Constants;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0702_2013" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0702_2013_MessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("cacheManager")
    private CacheManager cacheManager;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
        int icCardState = (Integer) messageBody.get("icCardState");
        if (icCardState == 1) {
            // 上班，qualification信息放缓存
            String qualification = (String) messageBody.get("qualification");
            cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_CURRENT_DRIVER_CACHE).put(deviceId, qualification);
        } else {
            // 下班，补充qualification信息
            Cache cache = cacheManager.getCache(com.legaoyi.common.util.Constants.CACHE_NAME_DEVICE_CURRENT_DRIVER_CACHE);
            ValueWrapper value = cache.get(deviceId);
            if (value != null) {
                String qualification = (String) value.get();
                messageBody.put("qualification", qualification);
                cache.evict(deviceId);
            }
        }
    }
}
