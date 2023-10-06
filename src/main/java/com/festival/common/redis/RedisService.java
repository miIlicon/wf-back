package com.festival.common.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisService {
    @Value("${custom.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final RedisTemplate<String, Object> redisTemplate;

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

    public void setRefreshToken(String username, String refreshToken){
        setData(username, refreshToken, refreshExpirationTime, TimeUnit.SECONDS);
    }

    public boolean isDuplicateAccess(String ipAddress, String domainName) {
        if(getData(ipAddress + "_" + domainName) == null)
            return false;
        return true;
    }
    public void setDuplicateAccess(String ipAddress, String domainName){
        setData(ipAddress + "_" + domainName, 1L, 1L, TimeUnit.DAYS);
    }
    public Set<String> getKeySet(String domain) {
        return redisTemplate.keys(domain);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    private void setData(String key, Object value, Long time,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value.toString(), time, timeUnit);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void rotateRefreshToken(String name, String refreshToken) {
        setRefreshToken(name, refreshToken);
    }
}
