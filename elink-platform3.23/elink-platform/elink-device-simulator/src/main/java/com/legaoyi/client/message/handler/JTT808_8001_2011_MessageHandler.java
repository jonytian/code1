package com.legaoyi.client.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import com.legaoyi.client.util.Constants;
import com.legaoyi.client.down.messagebody.Jt808_2011_8001_MessageBody;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.sender.MessageSender;
import com.legaoyi.client.up.messagebody.JTT808_0102_2011_MessageBody;

/**
 * 平台通用应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-7
 */
@Component(Constants.MESSAGE_HANDLER_BEAN_PREFIX + "8001_2011" + Constants.MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_8001_2011_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_8001_2011_MessageHandler.class);

    @Autowired
    @Qualifier("gpsMessageSender")
    private MessageSender gpsMessageSender;

    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws Exception {
        Jt808_2011_8001_MessageBody messageBody = (Jt808_2011_8001_MessageBody) message.getMessageBody();
        String messageId = messageBody.getMessageId();
        if (messageId.equals(JTT808_0102_2011_MessageBody.JTT808_MESSAGE_ID) && messageBody.getResult() == 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // 鉴权通过，开始发数据
                    try {
                        gpsMessageSender.send(ctx, message);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }).start();
        }
    }

}
