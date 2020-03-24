package com.legaoyi.persistence.redis.dao;

import java.util.List;

public interface RedisDao<T extends Object> {

    public T get(String key);

    public T getSet(String key, final T value);

    public boolean set(String key, final T value);

    public boolean setWithExpire(String key, final T value, final long seconds);

    public boolean setnx(String key, final T value);
    
    public int getInt(String key);

    public void del(String key);

    public boolean expire(String key, final long seconds);

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
