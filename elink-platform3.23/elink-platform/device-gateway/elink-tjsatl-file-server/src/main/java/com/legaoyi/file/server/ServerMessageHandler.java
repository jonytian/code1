package com.legaoyi.file.server;

import com.legaoyi.common.message.ExchangeMessage;

public interface ServerMessageHandler {

    public void handle(ExchangeMessage message) throws Exception;

}
