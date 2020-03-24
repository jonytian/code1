package com.legaoyi.gateway.message.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8001_MessageBody;
import com.legaoyi.protocol.exception.IllegalMessageException;
import com.legaoyi.protocol.exception.MessageDeliveryException;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.server.GatewayCacheManager;
import com.legaoyi.protocol.server.ServerMessageHandler;
import com.legaoyi.protocol.server.Session;
import com.legaoyi.protocol.server.SessionManager;
import com.legaoyi.protocol.server.SessionState;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0102_MessageBody;
import com.legaoyi.protocol.util.DefaultMessageBuilder;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
@Component("authResponseMessageHandler")
public class AuthResponseMessageHandler extends ExchangeMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthResponseMessageHandler.class);

    @Value("${tcp.closeSession.immediately}")
    private boolean closeSession = false;

    @Autowired
    @Qualifier("downstreamMessageDeliverer")
    private DownstreamMessageDeliverer downstreamMessageDeliverer;

    @Autowired
    @Qualifier("urgentUpstreamMessageHandler")
    private ServerMessageHandler urgentUpstreamMessageHandler;

    @Value("${multi.protocol}")
    private boolean multiProtocol = false;

    @Value("${defult.protocol.version}")
    private String defultJttProtocolVersion = Message.DEFULT_PROTOCOL_VERSION;

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage exchangeMessage) throws Exception {
        if (ExchangeMessage.MESSAGEID_PLATFORM_AUTH_RESP_MESSAGE.equals(exchangeMessage.getMessageId())) {
            Map<String, Object> map = (Map<String, Object>) exchangeMessage.getMessage();
            String simCode = (String) map.get("simCode");
            String authCode = (String) map.get("authCode");
            int result = (Integer) map.get("result");
            int messageSeq = (Integer) map.get("messageSeq");
            String protocolVersion = (String) map.get("protocolVersion");
            if (StringUtils.isBlank(protocolVersion)) {
                protocolVersion = defultJttProtocolVersion;
            }

            Session session = SessionManager.getInstance().getSession(simCode);
            if (session == null) {
                throw new MessageDeliveryException("device offline,simCode=".concat(simCode));
            }
            session.setProtocolVersion(protocolVersion);

            Message message = new Message();
            MessageHeader messageHeader = new MessageHeader();
            messageHeader.setSimCode(simCode);
            messageHeader.setMessageId(Jt808_2019_8001_MessageBody.MESSAGE_ID);
            message.setMessageHeader(messageHeader);
            Jt808_2019_8001_MessageBody messageBody = new Jt808_2019_8001_MessageBody();
            messageBody.setMessageId(Jt808_2019_0102_MessageBody.MESSAGE_ID);
            messageBody.setMessageSeq(messageSeq);
            messageBody.setResult(result);
            message.setMessageBody(messageBody);
            downstreamMessageDeliverer.deliver(session, message);

            // 鉴权成功
            if (result == 0) {
                session.setSessionState(SessionState.AUTHENTICATED);
                if (!multiProtocol) {// 多协议版本时，存在版本缓存同步问题，暂不缓存
                    // 鉴权码放缓存
                    gatewayCacheManager.addAuthCodeCache(simCode, authCode);
                }

                String messageNumLimit = (String) map.get("messageNumLimit");
                if (!StringUtils.isBlank(messageNumLimit)) {
                    try {
                        String arr[] = messageNumLimit.split(";");
                        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
                        Map<String, Integer> item;
                        int i = 1;
                        for (String s : arr) {
                            String a[] = s.split(",");
                            item = new HashMap<String, Integer>();
                            // 分钟
                            item.put("time", Integer.parseInt(a[0]) * 60 * 1000);
                            item.put("limit", Integer.parseInt(a[1]));
                            item.put("type", i++);
                            list.add(item);
                        }
                        session.setMessageNumLimit(list);
                    } catch (Exception e) {
                        logger.error("******parse message num limit error", e);
                    }
                }

                String messageByteLimit = (String) map.get("messageByteLimit");
                if (!StringUtils.isBlank(messageByteLimit)) {
                    try {
                        String arr[] = messageByteLimit.split(";");
                        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
                        Map<String, Integer> item;
                        int i = 1;
                        for (String s : arr) {
                            String a[] = s.split(",");
                            item = new HashMap<String, Integer>();
                            // 分钟
                            item.put("time", Integer.parseInt(a[0]) * 60 * 1000);
                            // M为单位
                            item.put("limit", Integer.parseInt(a[1]) * 1024 * 1024);
                            item.put("type", i++);
                            list.add(item);
                        }
                        session.setMessageByteLimit(list);
                    } catch (Exception e) {
                        logger.error("******parse message num limit error", e);
                    }
                }

                String upMessageLimit = (String) map.get("upMessageLimit");
                if (!StringUtils.isBlank(upMessageLimit)) {
                    String arr[] = upMessageLimit.split(",");
                    Set<String> upMessageLimitSet = new HashSet<String>();
                    for (String messageId : arr) {
                        upMessageLimitSet.add(messageId);
                    }
                    session.setUpMessageLimit(upMessageLimitSet);
                }

                // 通知平台上线
                urgentUpstreamMessageHandler.handle(DefaultMessageBuilder.buildOnlineMessage(message.getMessageHeader().getSimCode()));
            } else {
                // 鉴权失败是否需要关闭tcp连接?
                if (closeSession) {
                    session.getChannelHandlerContext().close();
                }
            }
        } else if (getSuccessor() != null) {
            getSuccessor().handle(exchangeMessage);
        } else {
            throw new IllegalMessageException();
        }
    }
}
