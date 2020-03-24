package com.legaoyi.client.message.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import com.legaoyi.client.util.Constants;
import com.legaoyi.client.util.MessageSeqGenerator;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.client.SpringBeanUtil;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.encoder.MessageEncoder;
import com.legaoyi.client.up.messagebody.JTT1078_1205_MessageBody;
import com.legaoyi.client.up.messagebody.JTT1078_1205_MessageBody.MediaResource;

/**
 * 平台通用应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-08-7
 */
@Component(Constants.MESSAGE_HANDLER_BEAN_PREFIX + "9205_1078" + Constants.MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT1078_9205_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT1078_9205_MessageHandler.class);

    @Value("${elink.device.protocol.version}")
    private String protocolVersion;

    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws Exception {
        JTT1078_1205_MessageBody messageBody = new JTT1078_1205_MessageBody();
        messageBody.setMessageSeq(message.getMessageHeader().getMessageSeq());

        List<MediaResource> resourceList = new ArrayList<MediaResource>();
        MediaResource mediaResource = messageBody.new MediaResource();
        mediaResource.setAlarmFlag("0000000000000000000000000000000000000000000000000000000000000000");
        mediaResource.setChannelId(1);
        mediaResource.setStartTime("2019-04-22 17:28:53");
        mediaResource.setFileSize(99900);
        mediaResource.setResourceType(0);
        mediaResource.setEndTime("2019-04-22 17:48:26");
        mediaResource.setStoreType(1);
        mediaResource.setStreamType(1);
        resourceList.add(mediaResource);

        mediaResource = messageBody.new MediaResource();
        mediaResource.setAlarmFlag("0000000000010000010000000000000000000000000000000000000000000000");
        mediaResource.setChannelId(1);
        mediaResource.setStartTime("2019-04-22 17:48:22");
        mediaResource.setFileSize(88800);
        mediaResource.setResourceType(0);
        mediaResource.setEndTime("2019-04-22 18:07:57");
        mediaResource.setStoreType(1);
        mediaResource.setStreamType(1);
        resourceList.add(mediaResource);

        messageBody.setResourceList(resourceList);
        message.getMessageHeader().setMessageId(JTT1078_1205_MessageBody.JTT808_MESSAGE_ID);
        message.getMessageHeader().setMessageSeq(MessageSeqGenerator.getNextSeq());
        message.setMessageBody(messageBody);

        logger.info("**********up message={}", message.toString());
        MessageBodyEncoder messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(message.getMessageHeader().getMessageId(), protocolVersion);
        List<byte[]> byteList = new MessageEncoder().encode(message, messageBodyEncoder);
        ctx.writeAndFlush(byteList);
    }

}
