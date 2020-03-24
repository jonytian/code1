
package com.legaoyi.protocol.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.protocol.message.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public abstract class BaseMessageChannelInboundHandler extends ChannelInboundHandlerAdapter {

    protected static final Logger logger = LoggerFactory.getLogger(BaseMessageChannelInboundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof Message) {
            Message message = (Message) msg;
            try {
                boolean bool = handle(ctx, message);
                if (bool) {
                    ctx.fireChannelRead(message);
                } else {
                    ReferenceCountUtil.release(msg);
                }
            } catch (Exception e) {
                ctx.fireChannelRead(message);
                logger.error("", e);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    protected abstract boolean handle(ChannelHandlerContext ctx, Message message);
}
