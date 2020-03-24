package com.legaoyi.gateway.security;

import java.io.Serializable;

/***
 * 
 * @author gaoshengbo
 *
 */
public class TokenBucket implements Serializable {

    private static final long serialVersionUID = 7997820220362125951L;

    private int capacity;

    private int tokenNum;

    private long lastRefillTimePoint;

    private String tokenBucketKey;

    private long increaseMillisecond = 1;

    private int increaseNum;

    private long increasePeriod;

    public final int getCapacity() {
        return capacity;
    }

    public final void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public final int getTokenNum() {
        return tokenNum;
    }

    public final void setTokenNum(int tokenNum) {
        this.tokenNum = tokenNum;
    }

    public final long getLastRefillTimePoint() {
        return lastRefillTimePoint;
    }

    public final void setLastRefillTimePoint(long lastRefillTimePoint) {
        this.lastRefillTimePoint = lastRefillTimePoint;
    }

    public final String getTokenBucketKey() {
        return tokenBucketKey;
    }

    public final void setTokenBucketKey(String tokenBucketKey) {
        this.tokenBucketKey = tokenBucketKey;
    }

    public final long getIncreaseMillisecond() {
        return increaseMillisecond;
    }

    public final void setIncreaseMillisecond(long increaseMillisecond) {
        this.increaseMillisecond = increaseMillisecond;
    }

    public final int getIncreaseNum() {
        return increaseNum;
    }

    public final void setIncreaseNum(int increaseNum) {
        this.increaseNum = increaseNum;
    }

    public final long getIncreasePeriod() {
        return increasePeriod;
    }

    public final void setIncreasePeriod(long increasePeriod) {
        this.increasePeriod = increasePeriod;
    }

    public final void filledToken(int num) {
        tokenNum += num;
    }

    public final void reduceToken(int num) {
        tokenNum -= num;
    }
}
