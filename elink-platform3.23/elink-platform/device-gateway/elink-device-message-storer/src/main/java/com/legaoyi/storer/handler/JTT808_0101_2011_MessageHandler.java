package com.legaoyi.storer.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.Constants;

/**
 * 终端发起注销，2011版本协议
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0101_2011" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0101_2011_MessageHandler extends MessageHandler {

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);
        // 设备状态,0:未注册；1:已注册；2:在线；3:离线;4:已注销；5：已停用
        deviceService.setDeviceStateUnregistered(simCode);
    }

}
