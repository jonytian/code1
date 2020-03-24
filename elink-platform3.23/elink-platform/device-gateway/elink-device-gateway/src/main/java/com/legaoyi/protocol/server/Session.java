package com.legaoyi.protocol.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.legaoyi.gateway.security.TokenBucket;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class Session {

    private String simCode;

    private String protocolVersion;

    private SessionState sessionState;

    private ChannelHandlerContext ctx = null;

    private Map<String, TokenBucket> tokenBucket = new HashMap<String, TokenBucket>();

    private Set<String> upMessageLimit;

    private List<Map<String, Integer>> messageNumLimit;

    private List<Map<String, Integer>> messageByteLimit;

    private long createTime;

    public Session(String simCode, ChannelHandlerContext ctx) {
        this.simCode = simCode;
        this.ctx = ctx;
        this.sessionState = SessionState.INITIATED;
    }

    public Session(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        this.sessionState = SessionState.CREATING;
        this.createTime = System.currentTimeMillis();
    }

    public final String getSimCode() {
        return simCode;
    }

    public final void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public final String getProtocolVersion() {
        return protocolVersion;
    }

    public final void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public final void setTokenBucket(String key, TokenBucket tokenBucket) {
        this.tokenBucket.put(key, tokenBucket);
    }

    public final Map<String, TokenBucket> getTokenBucket() {
        return this.tokenBucket;
    }

    public final TokenBucket getTokenBucket(String key) {
        return this.tokenBucket.get(key);
    }

    public final void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }

    public final SessionState getSessionState() {
        return sessionState;
    }

    public final ChannelHandlerContext getChannelHandlerContext() {
        return ctx;
    }

    public final long getCreateTime() {
        return createTime;
    }

    public final Set<String> getUpMessageLimit() {
        return upMessageLimit;
    }

    public final void setUpMessageLimit(Set<String> upMessageLimit) {
        this.upMessageLimit = upMessageLimit;
    }

    public final List<Map<String, Integer>> getMessageNumLimit() {
        return messageNumLimit;
    }

    public final void setMessageNumLimit(List<Map<String, Integer>> messageNumLimit) {
        this.messageNumLimit = messageNumLimit;
    }

    public final List<Map<String, Integer>> getMessageByteLimit() {
        return messageByteLimit;
    }

    public final void setMessageByteLimit(List<Map<String, Integer>> messageByteLimit) {
        this.messageByteLimit = messageByteLimit;
    }

}
