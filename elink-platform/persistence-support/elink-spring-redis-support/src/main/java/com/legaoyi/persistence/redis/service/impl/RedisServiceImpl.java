package com.legaoyi.persistence.redis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.legaoyi.persistence.redis.dao.RedisDao;
import com.legaoyi.persistence.redis.service.RedisService;

@Service("redisService")
public class RedisServiceImpl<T> implements RedisService<T> {

    @Autowired
    @Qualifier("redisDao")
    private RedisDao<T> redisDao;

    @Override
    public T get(String key) {
        return redisDao.get(key);
    }

    @Override
    public T getSet(String key, T value) {
        return redisDao.getSet(key, value);
    }

    @Override
    public boolean set(String key, T value) {
        return redisDao.setnx(key, value);
    }

    @Override
    public boolean setWithExpire(String key, T value, long seconds) {
        return redisDao.setWithExpire(key, value, seconds);
    }

    @Override
    public boolean setnx(String key, T value) {
        return redisDao.setnx(key, value);
    }

    @Override
    public int getInt(String key) {
        return redisDao.getInt(key);
    }

    @Override
    public void del(String key) {
        redisDao.del(key);
    }

    @Override
    public boolean expire(String key, long seconds) {
        return redisDao.expire(key, seconds);
    }

    @Override
    public List<String> zRangeByScore(String key, final String min, final String max) {
        return redisDao.zRangeByScore(key, min, max);
    }

    @Override
    public int generateSeq(final String key, final long max) {
        return redisDao.generateSeq(key, max);
    }

    @Override
    public long incr(String key) {
        return redisDao.incr(key);
    }

    @Override
    public long decr(String key) {
        return redisDao.decr(key);
    }

    @Override
    public boolean zAdd(String key, double score, String value) {
        return redisDao.zAdd(key, score, value);
    }

    @Override
    public boolean zRem(final String key, final String value) {
        return redisDao.zRem(key, value);
    }

    @Override
    public long sAdd(String key, String value) {
        return redisDao.sAdd(key, value);
    }

    @Override
    public List<String> sMembers(String key) {
        return redisDao.sMembers(key);
    }

    @Override
    public boolean sRem(String key, String value) {
        return redisDao.sRem(key, value);
    }

}
