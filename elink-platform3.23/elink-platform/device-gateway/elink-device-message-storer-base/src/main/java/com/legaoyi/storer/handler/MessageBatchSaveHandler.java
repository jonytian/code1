package com.legaoyi.storer.handler;

import com.legaoyi.common.disruptor.DisruptorMessageBatchHandler;
import com.legaoyi.storer.service.GeneralService;

/**
 * 批处理消息
 * 
 * @author gaoshengbo
 *
 */
public interface MessageBatchSaveHandler extends DisruptorMessageBatchHandler {

    public void registerBatchSaveService(String type,GeneralService service) throws Exception ;

}
