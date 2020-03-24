package com.legaoyi.storer.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.service.DeviceDownMessageService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.service.GeneralService;
import com.legaoyi.storer.util.BatchMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.ServerRuntimeContext;

@Component("messageBatchSaveHandler")
public class MessageBatchSaveHandlerAdapter implements MessageBatchSaveHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageBatchSaveHandlerAdapter.class);

    private static Map<String, GeneralService> saveServices = new ConcurrentHashMap<String, GeneralService>();

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("deviceDownMessageService")
    private DeviceDownMessageService deviceDownMessageService;

    @Autowired
    @Qualifier("deviceFaultService")
    private GeneralService deviceFaultService;

    @Override
    public void registerBatchSaveService(String type, GeneralService service) throws Exception {
        if (saveServices.containsKey(type)) {
            throw new Exception("该类型已存在！");
        }
        saveServices.put(type, service);
    }

    @Override
    public void handle(List<Object> list) {
        final Map<String, List<Object>> dataMap = new HashMap<String, List<Object>>();
        for (Object o : list) {
            if (o instanceof BatchMessage) {
                BatchMessage message = (BatchMessage) o;
                List<Object> ls = dataMap.get(message.getType());
                if (ls == null) {
                    ls = new ArrayList<Object>();
                    dataMap.put(message.getType(), ls);
                }
                ls.add(message.getMessage());
            } else {
                logger.info("batchSave unknow message,message={} ", o);
            }
        }

        for (String messageType : dataMap.keySet()) {
            List<Object> dataList = dataMap.get(messageType);
            GeneralService service = saveServices.get(messageType);
            try {
                if (service != null) {
                    service.batchSave(dataList);
                } else {
                    int type = Integer.valueOf(messageType);
                    switch (type) {
                        case Constants.BATCH_MESSAGE_TYPE_ON_OFF_LINE_LOG:
                            onOfflineLogsBatchSave(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_TYPE_DEVICE_STATE:
                            deviceStateBatchSave(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_DEVICE_DOWN_MESSAGE:
                            deviceDownMessageBatchSaveBatchSave(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_DEVICE_UP_MESSAGE:
                            deviceUpMessageBatchSave(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_TYPE_ACC_STATE_LOG:
                            accStateLogsBatchSave(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_TYPE_DEVICE_BIZ_STATE:
                            deviceService.batchSetDeviceBizState(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_TYPE_PARKING_LOG:
                            deviceService.saveDeviceParkingLogs(dataList);
                            break;
                        case Constants.BATCH_MESSAGE_TYPE_DEVICE_FAULT:
                            deviceFaultService.batchSave(dataList);
                            break;
                    }
                }
            } catch (Exception e) {
                logger.info("batchSave message error,message={} ", dataList, e);
            }
        }
    }

    private void deviceUpMessageBatchSave(List<Object> list) throws Exception {
        Map<String, List<Object>> dataMap = new HashMap<String, List<Object>>();
        for (Object o : list) {
            if (o instanceof ExchangeMessage) {
                ExchangeMessage message = (ExchangeMessage) o;
                Map<?, ?> map = (Map<?, ?>) message.getMessage();
                Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
                String messageId = (String) messageHeader.get(Constants.MAP_KEY_MESSAGE_ID);
                List<Object> ls = dataMap.get(messageId);
                if (ls == null) {
                    ls = new ArrayList<Object>();
                    dataMap.put(messageId, ls);
                }
                ls.add(message);
            } else {
                logger.info("batchSave unknow message,message={} ", o);
            }
        }

        GeneralService service;
        for (String messageId : dataMap.keySet()) {
            try {
                service = ServerRuntimeContext.getBean(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX.concat(messageId).concat(Constants.ELINK_MESSAGE_STORER_MESSAGE_SERVICE_BEAN_SUFFIX), GeneralService.class);
            } catch (Exception e) {
                logger.debug("can not found bean,save with default deviceUpMessageService ");
                service = ServerRuntimeContext.getBean("deviceUpMessageService", GeneralService.class);
            }

            if (service != null) {
                service.batchSave(dataMap.get(messageId));
            }
        }
    }

    private void deviceStateBatchSave(List<Object> list) throws Exception {
        Map<Short, List<Object>> dataMap = new HashMap<Short, List<Object>>();
        for (Object o : list) {
            Map<?, ?> map = (Map<?, ?>) o;
            short type = (Short) map.get("type");
            List<Object> ls = dataMap.get(type);
            if (ls == null) {
                ls = new ArrayList<Object>();
                dataMap.put(type, ls);
            }
            ls.add(o);
        }
        for (short type : dataMap.keySet()) {
            if (type == Constants.DEVICE_STATE_BATCH_SAVE_TYPE_ONLINE) {
                deviceService.batchSetDeviceStateOnline(dataMap.get(type));
            } else if (type == Constants.DEVICE_STATE_BATCH_SAVE_TYPE_OFFLINE) {
                deviceService.batchSetDeviceStateOffline(dataMap.get(type));
            } else if (type == Constants.DEVICE_STATE_BATCH_SAVE_TYPE_REGISTERED) {
                deviceService.batchSetDeviceStateRegistered(dataMap.get(type));
            }
        }
    }

    private void deviceDownMessageBatchSaveBatchSave(List<Object> list) throws Exception {
        deviceDownMessageService.batchSave(list);
    }

    private void onOfflineLogsBatchSave(List<Object> list) throws Exception {
        deviceService.saveDeviceOnOfflineLogs(list);
    }

    private void accStateLogsBatchSave(List<Object> list) throws Exception {
        deviceService.saveDeviceAccStateLogs(list);
    }
}
