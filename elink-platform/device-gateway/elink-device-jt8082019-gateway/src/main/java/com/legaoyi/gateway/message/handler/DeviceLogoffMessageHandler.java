package com.legaoyi.gateway.message.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.server.GatewayCacheManager;
import com.legaoyi.protocol.server.Session;
import com.legaoyi.protocol.server.SessionManager;
import com.legaoyi.common.message.ExchangeMessage;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2016-10-29
 */
@Component("deviceLogoffMessageHandler")
public class DeviceLogoffMessageHandler extends ExchangeMessageHandler {

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (ExchangeMessage.MESSAGEID_DEVICE_LOGOFF.equals(exchangeMessage.getMessageId())) {
            Map<String, Object> map = (Map<String, Object>) exchangeMessage.getMessage();
            String simCode = (String) map.get("simCode");
            gatewayCacheManager.removeAuthCodeCache(simCode);
            Session session = SessionManager.getInstance().getSession(simCode);
            if (session != null) {
                session.getChannelHandlerContext().close();
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        } else {
            throw new IllegalMessageException();
        }
    }
}
