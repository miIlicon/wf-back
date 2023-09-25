package com.festival.common.scheduler;

import com.festival.common.redis.RedisService;
import com.festival.domain.booth.service.BoothService;
import com.festival.domain.guide.service.GuideService;
import com.festival.domain.program.service.ProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
@EnableScheduling
public class SchedulerRunner {

    private final RedisService redisService;

    private final GuideService guideService;
    private final BoothService boothService;
    private final ProgramService programService;

    @Scheduled(fixedDelay = 3600000)
    public void updateViewCount()
    {
        Set<String> keySet = redisService.getKeySet("*Id*");
        for(String key : keySet){
            String[] splitKey = key.split("_");
            switch (splitKey[0]) {
                case "Booth" -> {
                    boothService.increaseBoothViewCount(redisService.getData(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
                case "Guide" -> {
                    guideService.increaseGuideViewCount(redisService.getData(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
                case "Program" -> {
                    programService.increaseProgramViewCount(redisService.getData(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateOperateStatus() {
        programService.settingProgramStatus();
        boothService.settingBoothStatus();
    }
}
