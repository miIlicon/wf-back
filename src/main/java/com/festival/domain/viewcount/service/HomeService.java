package com.festival.domain.viewcount.service;

import com.festival.common.redis.RedisService;
import com.festival.domain.viewcount.Home;
import com.festival.domain.viewcount.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final RedisService redisService;
    private final HomeRepository homeRepository;

    public void increase(String ipAddress) {
        if(!redisService.isDuplicateAccess(ipAddress, "Home")) {
            redisService.increaseRedisViewCount("viewCount_Home_1");
            redisService.setDuplicateAccess(ipAddress, "Home");
        }
    }

    @Transactional
    public void update(Long id, long count){
        Home home = homeRepository.findById(id).orElse(null);
        home.plus(count);
    }
}
