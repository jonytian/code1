package com.legaoyi;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageListenerManager;
import com.legaoyi.protocol.server.Server;
import com.legaoyi.protocol.server.ServerMessageHandler;
import com.legaoyi.protocol.util.ServerRuntimeContext;

@SuppressWarnings("rawtypes")
public class AppApplicationListener implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(AppApplicationListener.class);

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            stopServer();
        } else if (event instanceof ApplicationReadyEvent) {
            startServer();
        }
    }

    public void startServer() {
        restartNotify(1);
        ServerRuntimeContext.getBean("server", Server.class).start();
    }

    private void stopServer() {
        restartNotify(0);
        // 停止接收mq消息
        try {
            ServerRuntimeContext.getBean(MQMessageListenerManager.class).stopAll();
        } catch (Exception e) {
            logger.error("", e);
        }

        ServerRuntimeContext.getBean("server", Server.class).stop();
    }

    private void restartNotify(int result) {
        try {
            // 通知平台网关重启
            ServerMessageHandler urgentMessageHandler = ServerRuntimeContext.getBean("urgentUpstreamMessageHandler", ServerMessageHandler.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("result", result);
            urgentMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_RESTART, map, null));
        } catch (Exception e) {
            logger.error("******handle server restart message error", e);
        }
    }

}
