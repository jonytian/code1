package com.legaoyi.storer.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.service.ConfigService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.Constants;

@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0102_2011" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0102_2011_MessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0102_2011_MessageHandler.class);

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @Autowired
    @Qualifier("configService")
    private ConfigService configService;

    @Autowired
    public JTT808_0102_2011_MessageHandler(@Qualifier("deviceDownMessageSendHandler") MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);
        String authCode = null;
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        if (messageBody != null) {
            authCode = (String) messageBody.get("authCode");
        }

        int result = 0;
        String protocolVersion = null;
        Map<?, ?> device = (Map<?, ?>) message.getExtAttribute(Constants.MAP_KEY_DEVICE);
        if (authCode != null && device != null) {
            // 设备状态,0:未注册；1:已注册；2:在线；3:离线;4:已注销；5：已停用
            Integer state = (Integer) device.get("state");
            if (state != null && (state == 0 || state == 4 || state == 5)) {
                result = 1;
            }

            if (!authCode.equals((String) device.get("authCode"))) {
                result = 1;
            }
            protocolVersion = (String) device.get("protocolVersion");
        } else {
            result = 1;
        }

        Map<String, Object> resp = new HashMap<String, Object>();
        resp.put(Constants.MAP_KEY_SIM_CODE, simCode);// 终端唯一标识
        resp.put("result", result);// 鉴权结果,0是通过鉴权,1鉴权失败
        resp.put(Constants.MAP_KEY_MESSAGE_SEQ, messageHeader.get(Constants.MAP_KEY_MESSAGE_SEQ));// 对应终端的消息流水号

        if (result != 0) {
            logger.warn("authentication failure,message={}, deviceInfo={} ", message, device);
            messageBody.put("desc", "鉴权失败");
            messageBody.put("result", result);
        } else {
            messageBody.put("desc", "鉴权通过");
            resp.put("authCode", authCode);// 鉴权码
            if (protocolVersion != null) {
                resp.put("protocolVersion", protocolVersion);// 协议版本
            }
            Map<String, Object> config = configService.getEnterpriseConfig((String) device.get(Constants.MAP_KEY_ENTERPRISE_ID));
            if (config != null) {
                resp.put("messageNumLimit", config.get("messageNumLimit"));
                resp.put("messageByteLimit", config.get("messageByteLimit"));
                resp.put("upMessageLimit", config.get("upMessageLimit"));
            }
        }

        // 注入下发终端消息处理链
        ExchangeMessage exchangeMessage = new ExchangeMessage(ExchangeMessage.MESSAGEID_PLATFORM_AUTH_RESP_MESSAGE, resp, null, message.getGatewayId());
        exchangeMessage.setExtAttribute(message.getExtAttribute());
        getSuccessor().handle(exchangeMessage);
    }
}
