package com.legaoyi.persistence.redis.service;

import java.util.List;

public interface RedisService<T extends Object> {

    public T get(String key);

    public T getSet(String key, T value);

    public boolean set(String key, T value);

    public boolean setWithExpire(String key, T value, long seconds);

    public boolean setnx(String key, T value);

    public int getInt(String key);

    public void del(String key);

    public boolean expire(String key, long seconds);

    public List<String> zRangeByScore(String key, final String min, final String max);

    public int generateSeq(final String key, final long max);

    public long incr(String key);

    public long decr(String key);

    public boolean zAdd(String key, double score, String value);

    public boolean zRem(final String key, final String value);

    public long sAdd(String key, String value);

    public List<String> sMembers(String key);

    public boolean sRem(String key, String value);
}
