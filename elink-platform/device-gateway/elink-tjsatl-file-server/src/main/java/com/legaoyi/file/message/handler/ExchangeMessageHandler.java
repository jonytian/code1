package com.legaoyi.file.message.handler;

import com.legaoyi.common.message.ExchangeMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public abstract class ExchangeMessageHandler {

    private ExchangeMessageHandler successor;

    public ExchangeMessageHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(ExchangeMessageHandler successor) {
        this.successor = successor;
    }

    public abstract void handle(ChannelHandlerContext ctx,ExchangeMessage exchangeMessage) throws Exception;
}
