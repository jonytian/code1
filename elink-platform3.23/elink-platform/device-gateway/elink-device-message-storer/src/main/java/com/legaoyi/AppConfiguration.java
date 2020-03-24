package com.legaoyi;

import java.util.Observer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.legaoyi.common.disruptor.DisruptorEventBatchConsumer;
import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.storer.handler.MessageBatchSaveHandler;
import com.legaoyi.storer.handler.OnlineNotifyHandler;

@Configuration("appConfiguration")
@ImportResource(locations = {"classpath*:applicationContext*.xml"})
public class AppConfiguration {

    @Value("${lmax.disruptor.bufferSize}")
    private int bufferSize;

    @Autowired
    @Qualifier("messageBatchSaveHandler")
    private MessageBatchSaveHandler messageBatchSaveHandler;

    @Autowired
    @Qualifier("onlineNotifyHandler")
    private OnlineNotifyHandler onlineNotifyHandler;

    @Autowired
    @Qualifier("deviceOfflineDownMessageHandler")
    private Observer deviceOfflineDownMessageHandler;

    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
        return mongoTemplate;
    }

    @Bean("messageBatchSaveConsumer")
    public DisruptorEventBatchConsumer messageBatchSaveConsumer() {
        return new DisruptorEventBatchConsumer(messageBatchSaveHandler);
    }

    @Bean("messageBatchSaveProducer")
    public DisruptorEventBatchProducer messageBatchSaveProducer() {
        return new DisruptorEventBatchProducer(messageBatchSaveConsumer(), bufferSize);
    }

    @PostConstruct
    public void init() {
        onlineNotifyHandler.addObserver(deviceOfflineDownMessageHandler);
    }
}
