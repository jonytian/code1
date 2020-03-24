package com.legaoyi.protocol.server;

import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import com.legaoyi.gateway.security.TokenBucket;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-11-11
 */
@Component("gatewayCacheManager")
public class GatewayCacheManager {

    private static final String CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE = "elink_device_gateway_cache";

    @CachePut(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'device_token_bucket_'+#simCode")
    public Map<String, TokenBucket> addTokenBucketCache(String simCode, Map<String, TokenBucket> tokenBucket) throws Exception {
        return tokenBucket;
    }

    @CacheEvict(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'device_token_bucket_'+#simCode")
    public void removeTokenBucketCache(String simCode) throws Exception {}

    @Cacheable(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'device_token_bucket_'+#simCode", unless = "#result == null")
    public Map<String, TokenBucket> getTokenBucketCache(String simCode) {
        return null;
    }

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

    @CachePut(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'message_token_bucket_'+#simCode+'_'+#messageId")
    public TokenBucket addTokenBucketCache(String simCode, String messageId, TokenBucket tokenBucket) throws Exception {
        return tokenBucket;
    }

    @CacheEvict(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'message_token_bucket_'+#simCode+'_'+#messageId")
    public void removeTokenBucketCache(String simCode, String messageId) {}

    @Cacheable(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'message_token_bucket_'+#simCode+'_'+#messageId", unless = "#result == null")
    public TokenBucket getTokenBucketCache(String simCode, String messageId) {
        return null;
    }

    @CachePut(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'device_auth_code_'+#simCode")
    public String addAuthCodeCache(String simCode, String authCode) throws Exception {
        return authCode;
    }

    @Cacheable(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'device_auth_code_'+#simCode", unless = "#result == null")
    public String getAuthCodeCache(String simCode) {
        return null;
    }

    @CacheEvict(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'device_auth_code_'+#simCode")
    public void removeAuthCodeCache(String simCode) {}

    @CachePut(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'message_package_'+#cacheKey")
    public Packages addPackageCache(String cacheKey, Packages packages) {
        return packages;
    }

    @CacheEvict(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'message_package_'+#cacheKey")
    public void removePackageCache(String cacheKey) {}

    @Cacheable(value = CACHE_NAME_ELINK_DEVICE_GATEWAY_CACHE, key = "'message_package_'+#cacheKey", unless = "#result == null")
    public Packages getPackageCache(String cacheKey) {
        return null;
    }
}
