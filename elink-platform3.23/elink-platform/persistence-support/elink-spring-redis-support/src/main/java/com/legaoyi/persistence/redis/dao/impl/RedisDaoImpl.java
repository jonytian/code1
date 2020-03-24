package com.legaoyi.persistence.redis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import com.legaoyi.persistence.redis.dao.RedisDao;

@Component("redisDao")
public class RedisDaoImpl<T> implements RedisDao<T> {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public T get(String key) {
		final byte kb[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] value = connection.get(kb);
				if (value == null || value.length == 0) {
					return null;
				}
				return (T) SerializationUtils.deserialize(value);
			}
		});
	}

	@Override
	public boolean set(String key, final T value) {
		final byte bk[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(bk, SerializationUtils.serialize((Serializable) value));
				return true;
			}
		});
	}

	@Override
	public boolean setWithExpire(String key, final T value, final long seconds) {
		final byte bk[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(bk, SerializationUtils.serialize((Serializable) value));
				return connection.expire(bk, seconds);
			}
		});
	}

	@Override
	public boolean setnx(String key, final T value) {
		final byte bk[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(bk, SerializationUtils.serialize((Serializable) value));
			}
		});
	}

	@Override
	public T getSet(String key, final T value) {
		final byte bk[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				return (T) connection.getSet(bk, SerializationUtils.serialize((Serializable) value));
			}
		});
	}

	@Override
	public int getInt(String key) {
		final byte kb[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<Integer>() {

			@Override
			public Integer doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bytes = connection.get(kb);
				if (bytes != null && bytes.length > 0) {
					return Integer.parseInt(new String(bytes));
				}
				return 0;
			}
		});
	}

	@Override
	public void del(String key) {
		final byte bk[] = this.redisTemplate.getStringSerializer().serialize(key);
		this.redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.del(bk);
				return true;
			}
		});
	}

	@Override
	public boolean expire(String key, final long seconds) {
		final byte bk[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.expire(bk, seconds);
			}
		});
	}

	@Override
	public List<String> zRangeByScore(String key, final String min, final String max) {
		final RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
		final byte bk[] = serializer.serialize(key);
		return this.redisTemplate.execute(new RedisCallback<List<String>>() {

			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<byte[]> set = connection.zRangeByScore(bk, min, max);
				List<String> retList = new ArrayList<String>();
				if (set != null && !set.isEmpty()) {
					Iterator<byte[]> it = set.iterator();
					while (it.hasNext()) {
						retList.add(serializer.deserialize(it.next()));
					}
				}
				return retList;
			}
		});
	}

	@Override
	public int generateSeq(final String key, final long max) {
		final byte k[] = this.redisTemplate.getStringSerializer().serialize(key);
		return this.redisTemplate.execute(new RedisCallback<Integer>() {

			@Override
			public Integer doInRedis(RedisConnection connection) throws DataAccessException {
				long seq = connection.incr(k);
				if (seq >= max) {
					connection.del(k);
				}
				return (int) seq;
			}
		});
	}

	@Override
	public long incr(String key) {
		final byte k[] = redisTemplate.getStringSerializer().serialize(key);
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.incr(k);
			}
		});
	}

	@Override
	public long decr(String key) {
		final byte k[] = redisTemplate.getStringSerializer().serialize(key);
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.decr(k);
			}
		});
	}

	@Override
	public boolean zAdd(final String key, final double score, final String value) {
		final byte k[] = redisTemplate.getStringSerializer().serialize(key);
		final byte v[] = redisTemplate.getStringSerializer().serialize(value);
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zAdd(k, score, v);
			}
		});
	}

	@Override
	public boolean zRem(final String key, final String value) {
		final byte k[] = redisTemplate.getStringSerializer().serialize(key);
		final byte v[] = redisTemplate.getStringSerializer().serialize(value);
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zRem(k, v) > 0;
			}
		});
	}

	@Override
	public long sAdd(final String key, final String value) {
		final byte k[] = redisTemplate.getStringSerializer().serialize(key);
		final byte v[] = redisTemplate.getStringSerializer().serialize(value);
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.sAdd(k, v);
			}
		});
	}

	@Override
	public List<String> sMembers(String key) {
		final RedisSerializer<String> serializer = this.redisTemplate.getStringSerializer();
		final byte k[] = serializer.serialize(key);
		return redisTemplate.execute(new RedisCallback<List<String>>() {

			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				Set<byte[]> set = connection.sMembers(k);
				List<String> retList = new ArrayList<String>();
				if (set != null && !set.isEmpty()) {
					Iterator<byte[]> it = set.iterator();
					while (it.hasNext()) {
						retList.add(serializer.deserialize(it.next()));
					}
				}
				return retList;
			}
		});
	}

	@Override
	public boolean sRem(String key, String value) {
		final byte k[] = redisTemplate.getStringSerializer().serialize(key);
		final byte v[] = redisTemplate.getStringSerializer().serialize(value);
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.sRem(k, v);
			}
		}) > 0;
	}
}
