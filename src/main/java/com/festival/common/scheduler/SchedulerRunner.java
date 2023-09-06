package com.festival.common.scheduler;

import com.festival.common.redis.RedisService;
import com.festival.domain.guide.service.GuideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class SchedulerRunner {

    private final RedisService redisService;
    private final GuideService guideService;

    // 1 hour
    @Scheduled(fixedDelay = 3600000)
    public void updateViewCount()
    {
        log.info("updateViewCount");
        Set<String> keySet = redisService.getKeySet("*Id*");
        for(String key : keySet){
            String[] splitKey = key.split("_");
            switch (splitKey[0]) {
                case "Booth" -> {
                }
                case "Guide" -> {
                    guideService.increaseGuideViewCount(redisService.getData(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
                case "Program" -> {

                }

            }
        }

    }
}
