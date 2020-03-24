package com.legaoyi.file.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.file.server.GatewayCacheManager;

/***
 * 
 * @author gaoshengbo
 *
 */
@Component("securityUtil")
public class SecurityUtil {

    private static final String ATTRIBUTE_MSG_COUNT_TOKEN_BUCKET_KEY = "msg_count_tokenBucketKey";

    @Autowired
    @Qualifier("gatewayCacheManager")
    private GatewayCacheManager gatewayCacheManager;

    @Value("${message.count.limit}")
    private int limit;

    /***
     * 按IP每10分钟流量限制
     * 
     * @param ip
     * @param length
     * @param limit
     * @return
     */
    public boolean validateByIp(String ip, int num) {
        if (limit <= 0) {
            return true;
        }
        TokenBucket tokenBucket = gatewayCacheManager.getTokenBucketCacheByIp(ip);
        if (tokenBucket == null) {
            // 加载配置信息
            tokenBucket = createTokenBucket(ATTRIBUTE_MSG_COUNT_TOKEN_BUCKET_KEY, 10 * 60 * 1000, limit);
        }
        tokenBucket = filled(tokenBucket);

        boolean bool = false;
        int tokenNum = tokenBucket.getTokenNum();
        if (tokenNum >= num) {
            tokenBucket.reduceToken(num);
            bool = true;
        }
        try {
            gatewayCacheManager.addTokenBucketCacheByIp(ip, tokenBucket);
        } catch (Exception e) {
        }
        return bool;
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
