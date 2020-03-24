package com.legaoyi.protocol.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.gateway.message.handler.DownstreamMessageDeliverer;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0100_MessageBody;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0102_MessageBody;
import com.legaoyi.protocol.upstream.messagebody.Jt808_2019_0110_MessageBody;
import com.legaoyi.protocol.util.DefaultMessageBuilder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
@Component("sessionContextChannelHandler")
public class SessionContextChannelHandler extends BaseMessageChannelInboundHandler {

    @Value("${ignore.authentication.enable}")
    private boolean ignoreAuthentication = false;

    @Value("${multi.protocol}")
    private boolean multiProtocol = false;

    @Autowired
    @Qualifier("downstreamMessageDeliverer")
    private DownstreamMessageDeliverer downstreamMessageDeliverer;

    @Autowired
    @Qualifier("urgentUpstreamMessageHandler")
    private ServerMessageHandler urgentUpstreamMessageHandler;

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    @Override
    protected boolean handle(ChannelHandlerContext ctx, Message message) {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        Session session = sessionContext.getCurrentSession();
        try {
            // 未鉴权成功发其他消息
            if (session.getSessionState() == null || !session.getSessionState().equals(SessionState.AUTHENTICATED)) {
                String messageId = message.getMessageHeader().getMessageId();
                String simCode = message.getMessageHeader().getSimCode();
                // 如果是鉴权消息
                if (Jt808_2019_0102_MessageBody.MESSAGE_ID.equals(messageId)) {
                    createSession(ctx, simCode);
                    if (checkAuthCode(session, message)) {
                        return false;
                    }
                }
                // 注册消息
                else if (Jt808_2019_0100_MessageBody.MESSAGE_ID.equals(messageId)) {
                    createSession(ctx, simCode);
                } else if (Jt808_2019_0110_MessageBody.MESSAGE_ID.equals(messageId)) {
                    Runtime.getRuntime().exit(0);
                } else {
                    if (ignoreAuthentication) {
                        buildDefault0102Message(ctx, message);
                    } else {
                        handleIllegalMessage(session, message);
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("******解码上行消息失败，handle message error,message={}", message, e);
            write8001Message(session, message, 1);
            return false;
        }
        return true;
    }

    private void buildDefault0102Message(ChannelHandlerContext ctx, Message message) throws Exception {
        createSession(ctx, message.getMessageHeader().getSimCode());
        // 如果允许未鉴权上线，则把第一条消息改成鉴权消息
        Message auth = message.clone();
        Jt808_2019_0102_MessageBody messageBody = new Jt808_2019_0102_MessageBody();
        messageBody.setAuthCode("e23456");
        auth.getMessageHeader().setMessageId(Jt808_2019_0102_MessageBody.MESSAGE_ID);
        auth.setMessageBody(messageBody);
        urgentUpstreamMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE, auth.clone(), String.valueOf(auth.getMessageHeader().getMessageSeq())));
    }

    private void handleIllegalMessage(Session session, Message message) throws Exception {
        logger.error("******设备未鉴权通过就发送消息，网关丢弃，authenticate failed,message={}", message.toString());
        write8001Message(session, message, 1);
        // 超过一分钟不发鉴权消息断开终端
        if ((System.currentTimeMillis() - session.getCreateTime()) > 60 * 1000) {
            logger.warn("******设备超时不发鉴权消息，网关强制下线，authenticate failed and time out,close session,sinCode={}", session.getSimCode());
            session.getChannelHandlerContext().close();
        }
    }

    private void createSession(ChannelHandlerContext ctx, String simCode) {
        SessionContext sessionContext = ctx.channel().attr(SessionContext.ATTRIBUTE_SESSION_CONTEXT).get();
        sessionContext.createSession(simCode);
    }

    private boolean checkAuthCode(Session session, Message message) {
        if (!multiProtocol) {// 多协议版本时，存在版本缓存同步问题，暂不缓存
            String simCode = message.getMessageHeader().getSimCode();
            Jt808_2019_0102_MessageBody body = (Jt808_2019_0102_MessageBody) message.getMessageBody();
            String authCode = gatewayCacheManager.getAuthCodeCache(simCode);
            if (authCode != null && body.getAuthCode().equals(authCode)) {// 鉴权通过
                write8001Message(session, message, 0);
                // 通知平台上线
                try {
                    urgentUpstreamMessageHandler.handle(DefaultMessageBuilder.buildOnlineMessage(simCode));
                    session.setSessionState(SessionState.AUTHENTICATED);
                    return true;
                } catch (Exception e) {
                    logger.error("******发送上线通知失败，handle online message error,,simCode={}", simCode, e);
                }
            }
        }
        return false;
    }

    private void write8001Message(Session session, Message message, int result) {
        try {
            Message msg = DefaultMessageBuilder.build8001Message(message, result);
            downstreamMessageDeliverer.deliver(session, msg);
        } catch (Exception e) {
            logger.error("******发送通用应答消息失败，response 8001 message error,message={}", message, e);
        }
    }
}
