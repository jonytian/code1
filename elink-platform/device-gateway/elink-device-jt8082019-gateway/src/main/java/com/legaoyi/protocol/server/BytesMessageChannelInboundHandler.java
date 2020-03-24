
package com.legaoyi.protocol.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public abstract class BytesMessageChannelInboundHandler extends ChannelInboundHandlerAdapter {

    protected static final Logger logger = LoggerFactory.getLogger(BytesMessageChannelInboundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof byte[]) {
            byte[] bytes = (byte[]) msg;
            try {
                boolean bool = handle(ctx, bytes);
                if (bool) {
                    ctx.fireChannelRead(bytes);
                } else {
                    ReferenceCountUtil.release(msg);
                }
            } catch (Exception e) {
                ctx.fireChannelRead(bytes);
                logger.error("", e);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }

    protected abstract boolean handle(ChannelHandlerContext ctx, byte[] bytes);
}
