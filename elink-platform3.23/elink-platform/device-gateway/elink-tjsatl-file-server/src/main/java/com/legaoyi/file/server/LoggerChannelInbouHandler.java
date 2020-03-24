package com.legaoyi.file.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.file.messagebody.Attachment;
import com.legaoyi.protocol.util.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
@Component("loggerChannelInbouHandler")
public class LoggerChannelInbouHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoggerChannelInbouHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (logger.isInfoEnabled()) {
            if (msg instanceof byte[]) {
                logger.info("{}", ByteUtils.bytes2hex((byte[]) msg));
            } else if (msg instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf) msg;
                buf.markReaderIndex();
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                buf.resetReaderIndex();
                logger.info("{}", ByteUtils.bytes2hex(bytes));
            }else {
                if(msg instanceof Attachment) {
                    Attachment attachment= (Attachment)msg;
                    logger.info("fileName={},offset={},length={}", attachment.getFileName(),attachment.getOffset(),attachment.getLength());
                }else {
                    logger.info("{}", JsonUtil.covertObjectToString(msg));
                }
            }
        }
        ctx.fireChannelRead(msg);
    }
}
