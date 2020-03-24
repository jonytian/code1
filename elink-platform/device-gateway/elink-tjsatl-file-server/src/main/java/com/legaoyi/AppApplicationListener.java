package com.legaoyi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.legaoyi.file.server.Server;
import com.legaoyi.protocol.util.ServerRuntimeContext;

@SuppressWarnings("rawtypes")
public class AppApplicationListener implements ApplicationListener {

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
        ServerRuntimeContext.getBean("server", Server.class).start();
    }

    private void stopServer() {
        ServerRuntimeContext.getBean("server", Server.class).stop();
    }

   

}
