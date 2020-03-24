package com.legaoyi.gateway.message.handler;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.exception.MessageDeliveryException;
import com.legaoyi.protocol.exception.UnsupportedMessageException;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.server.Session;
import com.legaoyi.protocol.server.SessionManager;
import com.legaoyi.protocol.util.SpringBeanUtil;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
@Component("downstreamMessageHandler")
public class DownstreamMessageHandler extends ExchangeMessageHandler {

    @Value("${defult.protocol.version}")
    private String defultProtocolVersion = Message.DEFULT_PROTOCOL_VERSION;

    @Autowired
    @Qualifier("downstreamMessageDeliverer")
    private DownstreamMessageDeliverer downstreamMessageDeliverer;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (ExchangeMessage.MESSAGEID_PLATFORM_DOWN_MESSAGE.equals(exchangeMessage.getMessageId())) {
            Map<String, Object> map = (Map<String, Object>) exchangeMessage.getMessage();
            Map<String, Object> messageHeader = (Map<String, Object>) map.get("messageHeader");
            MessageHeader header = JsonUtil.convertStringToObject(JsonUtil.covertObjectToString(messageHeader), MessageHeader.class);
            Map<String, Object> messageBody = (Map<String, Object>) map.get("messageBody");

            Session session = SessionManager.getInstance().getSession(header.getSimCode());
            if (session == null) {
                throw new MessageDeliveryException("device offline,simCode=".concat(header.getSimCode()));
            }
            String protocolVersion = session.getProtocolVersion();
            if (StringUtils.isBlank(protocolVersion)) {
                protocolVersion = defultProtocolVersion;
            }
            String messageId = header.getMessageId();
            MessageBody body = null;
            try {
                body = SpringBeanUtil.getMessageBody(messageId, protocolVersion);
            } catch (Exception e) {
                throw new UnsupportedMessageException();
            }
            Message message = new Message();
            message.setMessageHeader(header);
            message.setMessageBody(body.invoke(messageBody));
            downstreamMessageDeliverer.deliver(session, message);
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        } else {
            throw new IllegalMessageException();
        }
    }

}
