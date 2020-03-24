package com.legaoyi.protocol.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
public class SessionContext {

    private Session session;

    public static final AttributeKey<SessionContext> ATTRIBUTE_SESSION_CONTEXT = AttributeKey.valueOf("sessionContext");

    public SessionContext(ChannelHandlerContext ctx) {
        session = SessionManager.getInstance().initSession(ctx);
    }

    public Session createSession(String simCode) {
        session = SessionManager.getInstance().createSession(simCode, session.getChannelHandlerContext());
        return session;
    }

    public Session getCurrentSession() {
        return session;
    }

    public void closeSession(String channelId) {
        if (session != null) {
            SessionManager.getInstance().closeSession(session.getSimCode(), channelId);
        }
    }
}
