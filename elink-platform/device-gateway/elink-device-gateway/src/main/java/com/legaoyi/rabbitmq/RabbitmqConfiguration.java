package com.legaoyi.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.legaoyi.common.disruptor.DisruptorEventConsumer;
import com.legaoyi.common.disruptor.DisruptorEventProducer;
import com.legaoyi.gateway.message.handler.ExchangeMessageHandler;
import com.legaoyi.gateway.message.handler.PlatformDownMessageHandler;
import com.legaoyi.mq.MQMessageHandler;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.protocol.server.ServerDisruptorMessageHandler;
import com.legaoyi.protocol.server.ServerUpMessageHandler;
import com.legaoyi.protocol.util.ServerRuntimeContext;

@Configuration("rabbitmqConfiguration")
public class RabbitmqConfiguration {

    @Value("${rabbitmq.device.up.message.exchange}")
    private String upMessageExchange;

    @Value("${rabbitmq.common.up.queue}")
    private String commonUpMessageQueue;

    @Value("${rabbitmq.urgent.up.queue}")
    private String urgentUpMessageQueue;

    @Value("${rabbitmq.media.up.queue}")
    private String mediaUpMessageQueue;

    @Value("${rabbitmq.message.durable}")
    private boolean durable = true;

    @Value("${lmax.disruptor.bufferSize}")
    private int bufferSize;

    @Autowired
    @Qualifier("serverRuntimeContext")
    protected ServerRuntimeContext serverRuntimeContext;

    @Autowired
    @Qualifier("serverDisruptorMessageHandler")
    private ServerDisruptorMessageHandler serverDisruptorMessageHandler;

    @Bean("commonUpMessageQueue")
    public Queue commonUpMessageQueue() {
        return new Queue(commonUpMessageQueue, durable);
    }

    @Bean("urgentUpMessageQueue")
    public Queue urgentUpMessageQueue() {
        return new Queue(urgentUpMessageQueue, durable);
    }

    @Bean("mediaUpMessageQueue")
    public Queue mediaUpMessageQueue() {
        return new Queue(mediaUpMessageQueue, durable);
    }

    @Bean("upMessageExchange")
    public DirectExchange upMessageExchange() {
        return new DirectExchange(upMessageExchange, durable, false);
    }

    @Bean("commonUpMessageQueueBinding")
    public Binding commonUpMessageQueueBinding() {
        return BindingBuilder.bind(commonUpMessageQueue()).to(upMessageExchange()).with("common");
    }

    @Bean("urgentUpMessageQueueBinding")
    public Binding urgentUpMessageQueueBinding() {
        return BindingBuilder.bind(urgentUpMessageQueue()).to(upMessageExchange()).with("urgent");
    }

    @Bean("mediaUpMessageQueueBinding")
    public Binding mediaUpMessageQueueBinding() {
        return BindingBuilder.bind(mediaUpMessageQueue()).to(upMessageExchange()).with("media");
    }

    @Bean("messageProducer")
    public MQMessageProducer initMessageProducer() {
        return new RabbitmqDirectExchangeMessageProducer(upMessageExchange);
    }

    @Bean("disruptorEventConsumer")
    public DisruptorEventConsumer initDisruptorEventConsumer() {
        return new DisruptorEventConsumer(serverDisruptorMessageHandler);
    }

    @Bean("disruptorEventProducer")
    public DisruptorEventProducer initDisruptorEventProducer() {
        return new DisruptorEventProducer(initDisruptorEventConsumer(), bufferSize);
    }

    @Bean("commonServerMessageHandler")
    public ServerUpMessageHandler initCommonServerMessageHandler() {
        ServerUpMessageHandler serverUpMessageHandler = new ServerUpMessageHandler();
        serverUpMessageHandler.setProducer(initDisruptorEventProducer());
        serverUpMessageHandler.setRoutingKey("common");
        return serverUpMessageHandler;
    }

    @Bean("urgentServerMessageHandler")
    public ServerUpMessageHandler initUrgentServerMessageHandler() {
        ServerUpMessageHandler serverUpMessageHandler = new ServerUpMessageHandler();
        serverUpMessageHandler.setProducer(initDisruptorEventProducer());
        serverUpMessageHandler.setRoutingKey("urgent");
        return serverUpMessageHandler;
    }

    @Bean("mediaServerMessageHandler")
    public ServerUpMessageHandler initMediaServerMessageHandler() {
        ServerUpMessageHandler serverUpMessageHandler = new ServerUpMessageHandler();
        serverUpMessageHandler.setProducer(initDisruptorEventProducer());
        serverUpMessageHandler.setRoutingKey("media");
        return serverUpMessageHandler;
    }

    @Bean("platformDownMessageHandler")
    public MQMessageHandler initPlatformDownMessageHandler() {
        PlatformDownMessageHandler platformDownMessageHandler = new PlatformDownMessageHandler();
        List<ExchangeMessageHandler> messageHandleList = new ArrayList<ExchangeMessageHandler>();
        messageHandleList.add(ServerRuntimeContext.getBean("deviceDownMessageHandler", ExchangeMessageHandler.class));
        messageHandleList.add(ServerRuntimeContext.getBean("authResponseMessageHandler", ExchangeMessageHandler.class));
        messageHandleList.add(ServerRuntimeContext.getBean("deviceLogoffMessageHandler", ExchangeMessageHandler.class));
        messageHandleList.add(ServerRuntimeContext.getBean("removeBlackListMessageHandler", ExchangeMessageHandler.class));
        platformDownMessageHandler.setMessageHandleList(messageHandleList);
        platformDownMessageHandler.setUrgentMessageHandler(initUrgentServerMessageHandler());
        return platformDownMessageHandler;
    }
}
