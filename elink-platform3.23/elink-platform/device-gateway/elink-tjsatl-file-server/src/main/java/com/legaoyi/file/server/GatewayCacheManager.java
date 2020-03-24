package com.legaoyi.file.server;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.legaoyi.file.server.security.TokenBucket;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-11-11
 */
@Component("gatewayCacheManager")
public class GatewayCacheManager {

    private static final String CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE = "elink_device_gateway_cache";

    @CachePut(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'ip_black_list_'+#ip")
    public Map<String, Object> addBlackListCache(String ip, Map<String, Object> info) throws Exception {
        return info;
    }

    @Cacheable(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'ip_black_list_'+#ip", unless = "#result == null")
    public Map<String, Object> getBlackListCache(String ip) {
        return null;
    }

    @CacheEvict(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'ip_black_list_'+#ip")
    public void removeBlackList(String ip) {}

    @CachePut(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'ip_token_bucket_'+#ip")
    public TokenBucket addTokenBucketCacheByIp(String ip, TokenBucket tokenBucket) throws Exception {
        return tokenBucket;
    }

    @Cacheable(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'ip_token_bucket_'+#ip", unless = "#result == null")
    public TokenBucket getTokenBucketCacheByIp(String ip) {
        return null;
    }

    @CacheEvict(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'ip_token_bucket_'+#ip")
    public void removeTokenBucketCacheByIp(String ip) {}
}
