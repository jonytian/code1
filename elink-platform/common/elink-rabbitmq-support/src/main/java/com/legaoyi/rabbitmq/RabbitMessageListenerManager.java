package com.legaoyi.rabbitmq;

import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.legaoyi.mq.MQMessageListenerManager;

@Component("rabbitMessageListenerManager")
public class RabbitMessageListenerManager implements MQMessageListenerManager {

    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Override
    public void startAll() {
        this.rabbitListenerEndpointRegistry.start();
    }

    @Override
    public void start(String queueName) {
        Collection<MessageListenerContainer> listenerContainers = this.rabbitListenerEndpointRegistry.getListenerContainers();
        for (MessageListenerContainer listenerContainer : listenerContainers) {
            if (!listenerContainer.isRunning() && this.isQueueListener(queueName, listenerContainer)) {
                listenerContainer.start();
            }
        }
    }

    @Override
    public void stopAll() {
        this.rabbitListenerEndpointRegistry.stop();
    }

    @Override
    public void stop(String queueName) {
        Collection<MessageListenerContainer> listenerContainers = this.rabbitListenerEndpointRegistry.getListenerContainers();
        for (MessageListenerContainer listenerContainer : listenerContainers) {
            if (listenerContainer.isRunning() && this.isQueueListener(queueName, listenerContainer)) {
                listenerContainer.stop();
            }
        }
    }

    /**
     * 判断监听器是否监听了指定的队列。
     * 
     * @param queueName 队列名称
     * @param listenerContainer 监听容器
     * @return true-监听，false-未监听。
     */
    private boolean isQueueListener(String queueName, MessageListenerContainer listenerContainer) {
        if (listenerContainer instanceof AbstractMessageListenerContainer) {
            AbstractMessageListenerContainer abstractMessageListenerContainer = (AbstractMessageListenerContainer) listenerContainer;
            String[] queueNames = abstractMessageListenerContainer.getQueueNames();
            return ArrayUtils.contains(queueNames, queueName);
        }
        return false;
    }
}
