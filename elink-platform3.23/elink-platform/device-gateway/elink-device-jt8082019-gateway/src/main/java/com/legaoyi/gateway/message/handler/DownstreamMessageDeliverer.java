package com.legaoyi.gateway.message.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.exception.MessageDeliveryException;
import com.legaoyi.protocol.exception.UnsupportedMessageException;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.encoder.MessageBodyEncoder;
import com.legaoyi.protocol.message.encoder.MessageEncoder;
import com.legaoyi.protocol.server.Session;
import com.legaoyi.protocol.server.SessionContext;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
@Component("downstreamMessageDeliverer")
public class DownstreamMessageDeliverer {

    @Value("${defult.protocol.version}")
    private String defultProtocolVersion = Message.DEFULT_PROTOCOL_VERSION;

    public void deliver(Session session, Message message) throws Exception {
        if (message == null) {
            return;
        }
        String simCode = message.getMessageHeader().getSimCode();
        if (session == null) {
            throw new MessageDeliveryException("device offline,simCode=".concat(simCode));
        }
        Channel channel = session.getChannelHandlerContext().channel();
        if (channel == null || !channel.isActive()) {
            throw new MessageDeliveryException("device offline,simCode=".concat(simCode));
        }
        MessageBodyEncoder messageBodyEncoder = null;
        String messageId = message.getMessageHeader().getMessageId();
        String protocolVersion = session.getProtocolVersion();
        if (StringUtils.isBlank(protocolVersion)) {
            protocolVersion = defultProtocolVersion;
        }

        try {
            messageBodyEncoder = SpringBeanUtil.getMessageBodyEncoder(messageId, protocolVersion);
        } catch (Exception e) {
            throw new UnsupportedMessageException(e);
        }

        List<byte[]> byteList = new MessageEncoder().encode(message, messageBodyEncoder);
        session.getChannelHandlerContext().writeAndFlush(byteList);
    }

    public void deliver(ChannelHandlerContext ctx, Message message) throws Exception {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();
        if (session != null) {
            this.deliver(session, message);
        }
    }

}
