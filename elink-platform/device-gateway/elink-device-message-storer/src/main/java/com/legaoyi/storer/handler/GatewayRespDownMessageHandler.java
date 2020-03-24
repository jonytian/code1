package com.legaoyi.storer.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.mq.MQMessageProducer;
import com.legaoyi.storer.service.DeviceDownMessageService;
import com.legaoyi.storer.service.DeviceService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.ServerRuntimeContext;
import com.legaoyi.storer.util.WebJmsMessage;

@Component("gatewayRespDownMessageHandler")
public class GatewayRespDownMessageHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(GatewayRespDownMessageHandler.class);

    private static final String ELINK_RESP_MESSAGE_Handler_BEAN_PREFIX = "gatewayResp_";

    private static final String ELINK_RESP_MESSAGE_Handler_BEAN__SUFFIX = "_messageHandler";

    @Autowired
    @Qualifier("deviceDownMessageService")
    private DeviceDownMessageService deviceDownMessageService;

    @Autowired
    @Qualifier("platformNotifyProducer")
    private MQMessageProducer platformNotifyProducer;

    @Autowired
    @Qualifier("deviceService")
    private DeviceService deviceService;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        if (message.getMessageId().equals(ExchangeMessage.MESSAGEID_GATEWAY_RESP_MESSAGE)) {
            Map<String, Object> resp = (Map<String, Object>) message.getMessage();
            // 0:成功；1：终端已经下线；其他：消息有误
            int result = (Integer) resp.get("result");
            try {
                String messageId = (String) resp.get(Constants.MAP_KEY_MESSAGE_ID);
                if (StringUtils.isNotBlank(messageId)) {
                    String simCode = (String) resp.get(Constants.MAP_KEY_SIM_CODE);
                    // 下行失败消息推动web页面
                    if (result != 0 && StringUtils.isNotBlank(simCode)) {
                        Map<String, Object> device = deviceService.getDeviceInfo(simCode);
                        if (device != null) {
                            resp.put(Constants.MAP_KEY_ENTERPRISE_ID, device.get(Constants.MAP_KEY_ENTERPRISE_ID));
                            resp.put(Constants.MAP_KEY_DEVICE_ID, device.get(Constants.MAP_KEY_DEVICE_ID));
                            
                            resp.put(Constants.MAP_KEY_DEVICE_NAME, device.get(Constants.MAP_KEY_DEVICE_NAME));
                            Object carId = device.get(Constants.MAP_KEY_CAR_ID);
                            if (carId != null) {
                                resp.put(Constants.MAP_KEY_CAR_ID, carId);
                                resp.put(Constants.MAP_KEY_PLATE_NUMBER, device.get(Constants.MAP_KEY_PLATE_NUMBER));
                            }
                            // 消息推送web页面
                            platformNotifyProducer.send(new WebJmsMessage(WebJmsMessage.DOWNSTREAM_ERROR_MESSAGE_TYPE, resp).toString());
                        }
                    }

                    // 检查是否有扩展的消息处理器
                    MessageHandler messageHandler = ServerRuntimeContext.getBean(ELINK_RESP_MESSAGE_Handler_BEAN_PREFIX.concat(messageId).concat(ELINK_RESP_MESSAGE_Handler_BEAN__SUFFIX), MessageHandler.class);
                    messageHandler.handle(message);
                }
            } catch (NoSuchBeanDefinitionException e) {
                try {
                    String exchangeId = message.getExchangeId();
                    if (StringUtils.isNotBlank(exchangeId)) {
                        deviceDownMessageService.setMessageState(result, exchangeId);
                    }
                } catch (Exception e1) {
                    logger.error("update downstream message state error,id={},state={}", message.getExchangeId(), result, e1);
                }
            } catch (Exception e) {
                logger.error(" handler response message error,message={}", message, e);
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(message);
        }
    }
}
