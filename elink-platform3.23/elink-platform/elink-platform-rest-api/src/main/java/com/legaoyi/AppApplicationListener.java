package com.legaoyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@SuppressWarnings("rawtypes")
public class AppApplicationListener implements ApplicationListener {

    /**
     * 1)ContextRefreshedEvent：当ApplicationContext初始化或者刷新时触发该事件。
     * 2)ContextClosedEvent：当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有单例Bean都被销毁
     * 3) RequestHandleEvent：在Web应用中，当一个http请求（request）结束触发该事件
     * 4)ContestStartedEvent：Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Start()方法开始/重新开始容器时触发该事件
     * 5)ContestStopedEvent：Spring2.5新增的事件，当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件
     * 至于之后是否新增的暂时没研究
     *
     * */
    private static final Logger logger = LoggerFactory.getLogger(AppApplicationListener.class);

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            stopServer();
        } else if (event instanceof ApplicationReadyEvent) {//监听容器初始化启动成功
            startServer();
        }
    }

    public void startServer() {
        logger.info("*******server started successfully");
    }

    private void stopServer() {
        logger.info("*******server stoped successfully");
    }

}
