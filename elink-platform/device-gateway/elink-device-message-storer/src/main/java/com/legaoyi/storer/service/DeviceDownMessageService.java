package com.legaoyi.storer.service;

public interface DeviceDownMessageService extends GeneralService {

    public void setMessageState(int state, String id) throws Exception;

}
