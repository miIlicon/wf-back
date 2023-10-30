package com.festival.domain.viewcount.util;

import com.festival.common.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class ViewCountUtil {

    private final RedisService redisService;

    public boolean isDuplicatedAccess(String ipAddress, String domainName) {
        return redisService.getData(ipAddress + "_" + domainName) != null;
    }

    public void setDuplicateAccess(String ipAddress, String domainName){
        redisService.setData(ipAddress + "_" + domainName, 1L, 1L, TimeUnit.DAYS);
    }

    public Long getViewCount(String key) {
        return Long.parseLong((String) redisService.getData(key));
    }

    public Set<String> getKeySet(String domain) {
        return redisService.getKeySet(domain);
    }

    public void deleteData(String key) {
        redisService.deleteData(key);
    }

    public void increaseData(String key) {
        redisService.increaseData(key);
    }

}
