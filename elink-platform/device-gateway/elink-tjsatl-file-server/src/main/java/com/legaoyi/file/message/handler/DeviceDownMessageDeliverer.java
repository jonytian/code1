package com.legaoyi.file.message.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;
import org.springframework.stereotype.Component;

import com.legaoyi.file.message.codec.MessageEncoder;
import com.legaoyi.file.server.util.Constants;
import com.legaoyi.protocol.exception.MessageDeliveryException;
import com.legaoyi.protocol.exception.UnsupportedMessageException;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Component("deviceDownMessageDeliverer")
public class DeviceDownMessageDeliverer {

    public void deliver(ChannelHandlerContext ctx, Message message) throws Exception {
        Channel channel = ctx.channel();
        if (channel == null || !channel.isActive()) {
            throw new MessageDeliveryException("device offline,simCode=".concat(message.getMessageHeader().getSimCode()));
        }

        MessageBodyEncoder messageBodyEncoder = null;
        String messageId = message.getMessageHeader().getMessageId();

        try {
            messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(messageId, Constants.PROTOCOL_VERSION);
        } catch (Exception e) {
            throw new UnsupportedMessageException(e);
        }

        List<byte[]> byteList = new MessageEncoder().encode(message, messageBodyEncoder);
        ctx.writeAndFlush(byteList);
    }

}
