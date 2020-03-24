package com.legaoyi.client.message.sender;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.client.SpringBeanUtil;
import com.legaoyi.client.message.Message;
import com.legaoyi.client.message.encoder.MessageEncoder;
import com.legaoyi.client.util.MessageSeqGenerator;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component("messageSender")
public class MessageSender {

    @Value("${elink.device.protocol.version}")
    private String protocolVersion;

    public void send(ChannelHandlerContext ctx, Message message) throws Exception {
        message.getMessageHeader().setMessageSeq(MessageSeqGenerator.getNextSeq());
        MessageBodyEncoder messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(message.getMessageHeader().getMessageId(), protocolVersion);
        List<byte[]> byteList = new MessageEncoder().encode(message, messageBodyEncoder);
        ctx.writeAndFlush(byteList);
    }
}
