package com.legaoyi.protocol.server;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-06-10
 */
public class SessionManager {

    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);

    private static SessionManager sessionManager = new SessionManager();

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    private SessionManager() {}

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public void closeSession(String sessionId, String channelId) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }
        Session session = sessionMap.get(sessionId);
        if (session == null || !session.getChannelHandlerContext().channel().id().asLongText().equals(channelId)) {
            return;
        }
        sessionMap.remove(sessionId);
        logger.warn("******closeSession,channelId={}, sessionId={}", channelId, sessionId);
    }

    public Session createSession(String sessionId, ChannelHandlerContext ctx) {
        String channelId = ctx.channel().id().asLongText();
        try {
            Session oldSession = sessionMap.get(sessionId);
            if (oldSession != null) {
                String oldChannelId = oldSession.getChannelHandlerContext().channel().id().asLongText();
                if (channelId.equals(oldChannelId)) {
                    return oldSession;
                } else {
                    oldSession.getChannelHandlerContext().close();
                    logger.warn("******new connection comming,forced offline,channelId={},sessionId={}", oldChannelId, sessionId);
                }
            }
        } catch (Exception e) {
        }

        Session session = new Session(sessionId, ctx);
        sessionMap.put(sessionId, session);
        logger.warn("******createSession,channelId={}, sessionId={}", channelId, sessionId);
        return session;
    }

    public Session initSession(ChannelHandlerContext ctx) {
        return new Session(ctx);
    }

    public Session getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public int size() {
        return sessionMap.size();
    }

    public void closeAll() {
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            try {
                entry.getValue().getChannelHandlerContext().close();
            } catch (Exception e) {
            }
        }
    }
}
