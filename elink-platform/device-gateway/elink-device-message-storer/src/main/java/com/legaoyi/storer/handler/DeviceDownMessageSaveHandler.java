package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.common.disruptor.DisruptorEventBatchProducer;
import com.legaoyi.common.message.ExchangeMessage;

/**
 * 终端下行消息存储处理
 * 
 * @author gaoshengbo
 *
 */
@Component("deviceDownMessageSaveHandler")
public class DeviceDownMessageSaveHandler extends MessageHandler {

    @Autowired
    @Qualifier("messageBatchSaveProducer")
    private DisruptorEventBatchProducer messageBatchSaveProducer;

    @Override
    @SuppressWarnings("unchecked")
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageHeader = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        if (messageHeader == null) {
            return;
        }
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        if (device != null) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
            data.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
            data.put("message", message.getMessage());
            messageBatchSaveProducer.produce(new BatchMessage(Constants.BATCH_MESSAGE_DEVICE_DOWN_MESSAGE, data));
        }
    }
}
