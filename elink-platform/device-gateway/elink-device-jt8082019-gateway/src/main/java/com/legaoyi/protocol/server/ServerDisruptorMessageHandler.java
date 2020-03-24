package com.legaoyi.protocol.server;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0801_MessageBody;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.common.util.DateUtils;
import com.legaoyi.common.util.FileUtil;
import com.legaoyi.common.disruptor.DisruptorMessageHandler;

/**
 * disruptor把消息发送mq
 * 
 * @author gaoshengbo
 *
 */
@Component("serverDisruptorMessageHandler")
public class ServerDisruptorMessageHandler implements DisruptorMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerDisruptorMessageHandler.class);

    @Autowired
    @Qualifier("messageProducer")
    private MQMessageProducer producer;

    @Value("${elink.gateway.id}")
    private String gatewayId;

    @Value("${message.threadPool.size}")
    private int threadPoolSize = 5;

    @Value("${media.store.path}")
    private String mediaStorePath;

    private ExecutorService fixedThreadPool = null;

    @PostConstruct
    public void init() {
        fixedThreadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    @Override
    public void handle(final Object message) {
        fixedThreadPool.execute(new Runnable() {

            public void run() {
                try {
                    String routingKey = null;
                    Object data;
                    if (message instanceof Map) {
                        Map<?, ?> map = (Map<?, ?>) message;
                        routingKey = (String) map.get("routingKey");
                        data = map.get("message");
                        if (data instanceof ExchangeMessage) {
                            ExchangeMessage exchangeMessage = (ExchangeMessage) data;
                            exchangeMessage.setGatewayId(gatewayId);
                            if (ExchangeMessage.MESSAGEID_GATEWAY_UP_MEDIA_MESSAGE.equals(exchangeMessage.getMessageId())) {
                                // 多媒体消息单独处理
                                resetMessage(exchangeMessage);
                            }
                        }
                    } else {
                        data = message;
                    }
                    producer.send(routingKey, data);
                } catch (Exception e) {
                    logger.error("******上行消息发送mq失败，send mq message error,message={}", message.toString(), e);
                }
            }
        });
    }

    private void resetMessage(ExchangeMessage exchangeMessage) throws Exception {
        Message message = (Message) exchangeMessage.getMessage();
        Jt808_2019_0801_MessageBody messageBody = (Jt808_2019_0801_MessageBody) message.getMessageBody();
        String desc = mediaStorePath;
        if (!desc.endsWith(File.separator)) {
            desc = desc.concat(File.separator);
        }

        int mediaFormatCode = messageBody.getMediaFormatCode();
        String suffix = null;
        if (mediaFormatCode == 0) {
            suffix = ".jpg";
        } else if (mediaFormatCode == 1) {
            suffix = ".tif";
        } else if (mediaFormatCode == 2) {
            suffix = ".mp3";
        } else if (mediaFormatCode == 3) {
            suffix = ".wav";
        } else if (mediaFormatCode == 4) {
            suffix = ".wmv";
        } else {
            suffix = ".mp4";
        }

        // 多媒体文件直接存储磁盘
        String path = message.getMessageHeader().getSimCode().concat(File.separator).concat(DateUtils.format(new Date(), "yyyyMMddHHmmss") + suffix);
        desc = desc.concat(path);
        FileUtil.writeWithMappedByteBuffer(desc, messageBody.getFileData());
        // 音视频只发布存储路径，上层应用来处理
        messageBody.setFilePath(desc);
    }
}
