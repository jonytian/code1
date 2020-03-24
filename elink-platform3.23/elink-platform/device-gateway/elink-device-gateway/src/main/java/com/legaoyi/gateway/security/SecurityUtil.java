package com.legaoyi.gateway.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.server.GatewayCacheManager;
import com.legaoyi.protocol.server.Session;
import com.legaoyi.protocol.server.SessionState;

/***
 * 
 * @author gaoshengbo
 *
 */
@Component("securityUtil")
public class SecurityUtil {

    public static final String ATTRIBUTE_MSG_COUNT_TOKEN_BUCKET_KEY = "msg_count_tokenBucketKey";

    public static final String ATTRIBUTE_MSG_DATA_TOKEN_BUCKET_KEY = "msg_data_tokenBucketKey";

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    /**
     * 
     * @param session
     * @param length
     * @param key
     * @param limitTime
     * @param limit
     * @return
     */
    public boolean validate(Session session, String key, int length, int limitTime, int limit) {
        SessionState sessionState = session.getSessionState();
        if (!sessionState.equals(SessionState.AUTHENTICATED)) {
            return true;
        }

        TokenBucket tokenBucket = session.getTokenBucket(key);
        if (null == tokenBucket) {
            String simCode = session.getSimCode();
            if (simCode != null && !"".equals(simCode)) {
                tokenBucket = getCacheTokenBucket(simCode, key);
            }
        }
        if (null == tokenBucket) {
            // 加载配置信息
            tokenBucket = createTokenBucket(key, limitTime, limit);
        }
        boolean bool = false;
        tokenBucket = filled(tokenBucket);
        int tokenNum = tokenBucket.getTokenNum();
        if (tokenNum >= length) {
            tokenBucket.reduceToken(length);
            bool = true;
        }
        session.setTokenBucket(key, tokenBucket);
        return bool;
    }

    /***
     * 按终端设备每分钟消息条数限制
     * 
     * @param session
     * @param limit
     * @return
     */
    public boolean validate(Session session, int limit) {
        return validate(session, ATTRIBUTE_MSG_COUNT_TOKEN_BUCKET_KEY, 1, 1 * 60 * 1000, limit);
    }

    /***
     * 按终端设备每分钟消息条数限制
     * 
     * @param session
     * @param length
     * @param limit
     * @return
     */
    public boolean validate(Session session, int length, int limit) {
        return validate(session, ATTRIBUTE_MSG_DATA_TOKEN_BUCKET_KEY, length, 1 * 60 * 1000, limit);
    }

    /***
     * 按IP每分钟流量限制
     * 
     * @param ip
     * @param length
     * @param limit
     * @return
     */
    public boolean validateByIp(String ip, int length, int limit) {
        TokenBucket tokenBucket = gatewayCacheManager.getTokenBucketCacheByIp(ip);
        if (tokenBucket == null) {
            // 加载配置信息
            tokenBucket = createTokenBucket(ATTRIBUTE_MSG_DATA_TOKEN_BUCKET_KEY, 1 * 60 * 1000, limit * 1024);
        }
        tokenBucket = filled(tokenBucket);

        boolean bool = false;
        int tokenNum = tokenBucket.getTokenNum();
        if (tokenNum >= length) {
            tokenBucket.reduceToken(length);
            bool = true;
        }
        try {
            gatewayCacheManager.addTokenBucketCacheByIp(ip, tokenBucket);
        } catch (Exception e) {
        }
        return bool;
    }

    /**
     * 按终端设备每种消息类型每每十分钟消息条数限制
     * 
     * @param simCode
     * @param messageId
     * @param limit
     * @return
     */
    public boolean validateByMessageId(String simCode, String messageId, int limit) {
        TokenBucket tokenBucket = gatewayCacheManager.getTokenBucketCache(simCode, messageId);
        if (tokenBucket == null) {
            // 加载配置信息
            tokenBucket = createTokenBucket(ATTRIBUTE_MSG_COUNT_TOKEN_BUCKET_KEY, 10 * 60 * 1000, limit);
        }
        tokenBucket = filled(tokenBucket);

        boolean bool = false;
        int tokenNum = tokenBucket.getTokenNum();
        if (tokenNum >= 1) {
            tokenBucket.reduceToken(1);
            bool = true;
        }
        try {
            gatewayCacheManager.addTokenBucketCache(simCode, messageId, tokenBucket);
        } catch (Exception e) {
        }
        return bool;
    }

    private TokenBucket getCacheTokenBucket(String simCode, String key) {
        Map<String, TokenBucket> map = gatewayCacheManager.getTokenBucketCache(simCode);
        if (null != map) {
            return map.get(key);
        }
        return null;
    }

    private TokenBucket createTokenBucket(String tokenBucketKey, int limitTime, int limit) {
        TokenBucket tokenBucket = new TokenBucket();
        tokenBucket.setCapacity(limit);
        tokenBucket.setIncreaseMillisecond(limitTime);
        tokenBucket.setIncreaseNum(limit);
        tokenBucket.setIncreasePeriod(limitTime);
        tokenBucket.setLastRefillTimePoint(System.currentTimeMillis());
        tokenBucket.setTokenBucketKey(tokenBucketKey);
        tokenBucket.setTokenNum(limit);
        return tokenBucket;
    }

    private TokenBucket filled(TokenBucket tokenBucket) {
        long now = System.currentTimeMillis();
        long lastRefillTimePoint = tokenBucket.getLastRefillTimePoint();

        long increasePeriod = tokenBucket.getIncreasePeriod();
        long increaseMillisecond = tokenBucket.getIncreaseMillisecond();
        if (increasePeriod == 0 || increasePeriod < increaseMillisecond) {
            increasePeriod = increaseMillisecond;
        }
        long nextRefillTimePoint = lastRefillTimePoint + increasePeriod;
        if (now < nextRefillTimePoint) {
            return tokenBucket;
        }
        int numPeriods = (int) Math.max(0, (now - lastRefillTimePoint) / increaseMillisecond);

        lastRefillTimePoint += numPeriods * increaseMillisecond;
        tokenBucket.setLastRefillTimePoint(lastRefillTimePoint);
        int increaseNum = numPeriods * tokenBucket.getIncreaseNum();
        filledToken(tokenBucket, increaseNum);
        return tokenBucket;
    }

    private void filledToken(TokenBucket tokenBucket, int increaseNum) {
        int capacity = tokenBucket.getCapacity();
        int newTokens = Math.min(capacity, Math.max(0, increaseNum));
        int maxAddToken = capacity - tokenBucket.getTokenNum();
        tokenBucket.filledToken(Math.max(0, Math.min(maxAddToken, newTokens)));
    }

}
