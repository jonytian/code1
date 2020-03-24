package com.legaoyi.file.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.legaoyi.protocol.util.ByteUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@ChannelHandler.Sharable
@Component("loggerChannelOutbouHandler")
public class LoggerChannelOutbouHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoggerChannelOutbouHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        if (logger.isInfoEnabled()) {
            if (msg instanceof List) {
                List<?> messageList = (List<?>) msg;
                for (Object o : messageList) {
                    if (o instanceof byte[]) {
                        byte[] bytes = (byte[]) o;
                        logger.info("{}", ByteUtils.bytes2hex(bytes));
                    }
                }
            }
        }
        ctx.write(msg, promise);
    }

}
