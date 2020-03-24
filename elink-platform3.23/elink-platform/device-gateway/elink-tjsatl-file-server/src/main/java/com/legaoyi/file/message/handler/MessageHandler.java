package com.legaoyi.file.message.handler;

import com.legaoyi.protocol.message.Message;

import io.netty.channel.ChannelHandlerContext;

public interface MessageHandler {

    public void handle(ChannelHandlerContext ctx, Message message) throws Exception;
}
