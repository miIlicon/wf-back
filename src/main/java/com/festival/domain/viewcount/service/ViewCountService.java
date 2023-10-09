package com.festival.domain.viewcount.service;

import com.festival.common.redis.RedisService;
import com.festival.domain.viewcount.ViewCount;
import com.festival.domain.viewcount.repository.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ViewCountService {
    private final RedisService redisService;
    private final ViewCountRepository viewCountRepository;


    public void increase(String ipAddress) {
        if(!redisService.isDuplicateAccess(ipAddress, "ViewCount")) {
            redisService.increaseRedisViewCount("ViewCount_Id_1");
            redisService.setDuplicateAccess(ipAddress, "ViewCount");
        }
    }
    @Transactional
    public void update(Long id, long count){
        ViewCount viewCount = viewCountRepository.findById(id).orElse(null);
        viewCount.plus(count);
    }
}
