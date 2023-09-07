package com.festival.common.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Long> redisTemplate;

    @PostConstruct
    public void initialize() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void increaseRedisViewCount(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public void decreaseRedisViewCount(String key) {
        redisTemplate.opsForValue().decrement(key);
    }

    public boolean isDuplicateAccess(String ipAddress, String domainName) {
        if(getData(ipAddress + "_" + domainName) == null) {
            setData(ipAddress + "_" + domainName, 1L, TimeUnit.DAYS);
            return true;
        }
        return false;
    }

    public Set<String> getKeySet(String domain) {
        return redisTemplate.keys(domain);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    private void setData(String key, Long value, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, 1L, timeUnit);
    }

    public Long getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
