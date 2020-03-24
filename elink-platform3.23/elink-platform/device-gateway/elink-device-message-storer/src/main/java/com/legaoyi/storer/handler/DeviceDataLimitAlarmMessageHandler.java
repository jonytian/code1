package com.legaoyi.storer.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.storer.service.AlarmService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.util.WebJmsMessage;

@Component("deviceDataLimitAlarmMessageHandler")
public class DeviceDataLimitAlarmMessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("alarmService")
    private AlarmService alarmService;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_DATA_LIMIT_ALARM)) {
            Map<String, Object> map = (Map<String, Object>) message.getMessage();
            String simCode = (String) map.get(Constants.MAP_KEY_SIM_CODE);
            if (simCode != null) {
                Map<String, Object> device = deviceService.getDeviceInfo(simCode);
                String deviceId = (String) device.get(Constants.MAP_KEY_DEVICE_ID);
                map.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
                map.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
                map.put(Constants.MAP_KEY_DEVICE_ID, deviceId);
                map.put(Constants.MAP_KEY_SIM_CODE, simCode);
            } else {
                //顶级运营商企业id
                map.put(Constants.MAP_KEY_ENTERPRISE_ID, "92828");
                map.put(Constants.MAP_KEY_DEVICE_ID, "");
            }
            map.put("gatewayId", message.getGatewayId());
            map.put("alarmTime", message.getCreateTime());
            alarmService.saveDataLimitAlarm(map);
            // 线消息推送到web页面
            platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.DATA_LIMIT_ALARM_MESSAGE_TYPE, map).toString());
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
