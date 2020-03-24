package com.legaoyi.gateway.message.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.server.GatewayCacheManager;
import com.legaoyi.common.message.ExchangeMessage;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-07-24
 */
@Component("removeBlackListMessageHandler")
public class RemoveBlackListMessageHandler extends ExchangeMessageHandler {

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (ExchangeMessage.MESSAGEID_PLATFORM_DOWN_REMOVE_BLACK_LIST_MESSAGE.equals(exchangeMessage.getMessageId())) {
            Map<String, Object> map = (Map<String, Object>) exchangeMessage.getMessage();
            String ip = (String) map.get("ip");
            if (ip != null) {
                gatewayCacheManager.removeBlackList(ip);
                gatewayCacheManager.removeTokenBucketCacheByIp(ip);
            }
            String simCode = (String) map.get("simCode");
            if (simCode != null) {
                gatewayCacheManager.removeTokenBucketCache(simCode);
            }

            String messageIds = (String) map.get("messageId");
            if (simCode != null && messageIds != null) {
                String[] arr = messageIds.split(",");
                for (String messageId : arr) {
                    gatewayCacheManager.removeTokenBucketCache(simCode, messageId);
                }
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        } else {
            throw new IllegalMessageException();
        }
    }
}
