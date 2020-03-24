package com.legaoyi.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.legaoyi.mq.MQMessageHandler;
import com.legaoyi.storer.handler.GatewayUpMessageHandler;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.ServerRuntimeContext;

@Component("commonUpMessageRabbitmqListener")
@RabbitListener(queues = "${rabbitmq.common.up.queue}")
public class CommonUpMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(CommonUpMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Value("${rabbitmq.common.up.queue}")
    private String commonUpMessageQueue;

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            commonUpMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @RabbitHandler
    public void onMessage(String json) {
        try {
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            commonUpMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @Bean("commonUpMessageQueue")
    public Queue commonUpMessageQueue() {
        return new Queue(commonUpMessageQueue, durable);
    }

    @Bean("commonUpMessageHandler")
    public MQMessageHandler commonUpMessageHandler() {
        GatewayUpMessageHandler handler = new GatewayUpMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("deviceUpMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
