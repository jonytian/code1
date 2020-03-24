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

import com.legaoyi.storer.handler.GatewayUpMessageHandler;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.util.ServerRuntimeContext;
import com.legaoyi.mq.MQMessageHandler;

@Component("mediaUpMessageRabbitmqListener")
@RabbitListener(queues = "${rabbitmq.media.up.queue}")
public class MediaUpMessageRabbitmqListener {

    private static final Logger logger = LoggerFactory.getLogger(MediaUpMessageRabbitmqListener.class);

    private static final String DEFAULT_CHARSET = "UTF-8";

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Value("${rabbitmq.media.up.queue}")
    private String mediaUpMessageQueue;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @RabbitHandler
    public void onMessage(byte[] bytes) {
        String json = null;
        try {
            json = new String(bytes, DEFAULT_CHARSET);
            if (logger.isInfoEnabled()) {
                logger.info(json);
            }
            deviceUpMediaMessageHandler().handle(json);
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
            deviceUpMediaMessageHandler().handle(json);
        } catch (Exception e) {
            logger.error("handle mq Message error,message={}", json, e);
        }
    }

    @Bean("mediaUpMessageQueue")
    public Queue mediaUpMessageQueue() {
        return new Queue(mediaUpMessageQueue, durable);
    }

    @Bean("deviceUpMediaMessageHandler")
    public MQMessageHandler deviceUpMediaMessageHandler() {
        GatewayUpMessageHandler handler = new GatewayUpMessageHandler();
        List<MessageHandler> handlers = new ArrayList<MessageHandler>();
        handlers.add(ServerRuntimeContext.getBean("deviceUpMessageHandler", MessageHandler.class));
        handler.setHandlers(handlers);
        return handler;
    }

}
