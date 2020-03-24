package com.legaoyi.protocol.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.util.ByteUtils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
@Component("loggerChannelInbouHandler")
public class LoggerChannelInbouHandler extends BytesMessageChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoggerChannelInbouHandler.class);

    @Override
    public boolean handle(ChannelHandlerContext ctx, byte[] bytes) {
        if (logger.isInfoEnabled()) {
            logger.info("{}", ByteUtils.bytes2hex(bytes));
        }
        return true;
    }
}
