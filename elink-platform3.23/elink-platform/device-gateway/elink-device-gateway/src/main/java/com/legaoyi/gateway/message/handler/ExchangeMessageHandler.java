package com.legaoyi.gateway.message.handler;

import com.legaoyi.common.message.ExchangeMessage;

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

    public abstract void handle(ExchangeMessage exchangeMessage) throws Exception;
}
