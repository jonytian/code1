package com.legaoyi.protocol.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.protocol.downstream.messagebody.Jt808_2019_8001_MessageBody;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.message.MessageHeader;
import com.legaoyi.protocol.server.Session;
import com.legaoyi.protocol.server.SessionContext;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
public class DefaultMessageBuilder {

    private static ExchangeMessage buildLinkStateMessage(String simCode, int state) {
        Map<String, Object> stateMap = new HashMap<String, Object>();
        stateMap.put("simCode", simCode);
        stateMap.put("state", state);
        return new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_LINK_STATUS_MESSAGE, stateMap, null);
    }

    public static ExchangeMessage buildOnlineMessage(String simCode) {
        return buildLinkStateMessage(simCode, 1);
    }

    public static ExchangeMessage buildOfflineMessage(String simCode) {
        return buildLinkStateMessage(simCode, 0);
    }

    public static Message build8001Message(ChannelHandlerContext ctx, int result) {
        String simCode = null;
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        if (sessionContext != null) {
            Session session = sessionContext.getCurrentSession();
            if (session != null) {
                simCode = session.getSimCode();
            }
        }

        if (StringUtils.isBlank(simCode)) {
            simCode = "000000000000";
        }

        Message message = new Message();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setMessageId(Jt808_2019_8001_MessageBody.MESSAGE_ID);
        messageHeader.setSimCode(simCode);

        Jt808_2019_8001_MessageBody messageBody = new Jt808_2019_8001_MessageBody();
        messageBody.setMessageId("0000");
        messageBody.setMessageSeq(0);
        messageBody.setResult(result);

        message.setMessageHeader(messageHeader);;
        message.setMessageBody(messageBody);
        return message;
    }

    public static Message build8001Message(Message message, int result) {
        if (message == null) {
            return null;
        }
        Jt808_2019_8001_MessageBody messageBody = new Jt808_2019_8001_MessageBody();
        messageBody.setMessageId(message.getMessageHeader().getMessageId());
        messageBody.setMessageSeq(message.getMessageHeader().getMessageSeq());
        messageBody.setResult(result);
        message.getMessageHeader().setMessageId(Jt808_2019_8001_MessageBody.MESSAGE_ID);
        message.setMessageBody(messageBody);
        return message;
    }
}
