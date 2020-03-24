package com.legaoyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.legaoyi.client.Client;
import com.legaoyi.protocol.util.ServerRuntimeContext;

@SuppressWarnings("rawtypes")
public class AppApplicationListener implements ApplicationListener {

    private static final Logger logger = LoggerFactory.getLogger(AppApplicationListener.class);

    @Autowired
    protected ServerRuntimeContext serverRuntimeContext;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            logger.info("******simulator stopping*****");
        } else if (event instanceof ApplicationReadyEvent) {
            Client client = (Client) ServerRuntimeContext.getBean("client");
            try {
                client.start();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

}
