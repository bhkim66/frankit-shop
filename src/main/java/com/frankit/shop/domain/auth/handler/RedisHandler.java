package com.frankit.shop.domain.auth.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
@RequiredArgsConstructor
public class RedisHandler {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, String value, Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, MILLISECONDS);
    }

    public void setHashData(String key, Map<String, Object> value, Long expiredTime){
        HashOperations<String, String, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.putAll(key, value);
        redisTemplate.expire(key, expiredTime, MILLISECONDS);
    }

    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public String getHashData(String key, String hashKey){
        return (String) redisTemplate.opsForHash().get(key, hashKey);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}
