package com.legaoyi.client.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import com.legaoyi.client.util.Constants;
import com.legaoyi.file.client.FileClient;
import com.legaoyi.protocol.util.ServerRuntimeContext;
import com.legaoyi.client.down.messagebody.Tjsatl_2017_9208_MessageBody;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.sender.MessageSender;

/**
 * 平台通用应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-7
 */
@Component(Constants.MESSAGE_HANDLER_BEAN_PREFIX + "9208_tjsatl" + Constants.MESSAGE_HANDLER_BEAN_SUFFIX)
public class Tjsatl_2017_9208_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Tjsatl_2017_9208_MessageHandler.class);

    @Autowired
    @Qualifier("gpsMessageSender")
    private MessageSender gpsMessageSender;

    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws Exception {
        Tjsatl_2017_9208_MessageBody messageBody = (Tjsatl_2017_9208_MessageBody) message.getMessageBody();
        logger.info("********messageBody={}", messageBody.toString());
        String ip = messageBody.getIp();
        int tcpPort = messageBody.getTcpPort();
        FileClient fileClient = (FileClient) ServerRuntimeContext.getBean("fileClient");
        try {
            fileClient.start(ip, tcpPort, messageBody.getAlarmId());
        } catch (Exception e) {
            logger.error("", e);
        }
    }

}
