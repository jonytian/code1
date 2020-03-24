package com.legaoyi.storer.handler;

import java.util.Observable;

import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;

@Component("onlineNotifyHandler")
public class OnlineNotifyHandler extends Observable {

    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        this.setChanged();
        this.notifyObservers(exchangeMessage);
    }
}
