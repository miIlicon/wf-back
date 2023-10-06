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

    /**
     * @Description
     * 설정한 업데이트 주기에 따라 Redis에 쌓여있던 조회수를 RDB에 한번에 반영합니다.
     */
    @Scheduled(fixedDelay = 3600000)
    public void updateViewCount()
    {
        Set<String> keySet = redisService.getKeySet("*Id*");
        for(String key : keySet){
            String[] splitKey = key.split("_");
            switch (splitKey[0]) {
                case "Booth" -> {
                    boothService.increaseBoothViewCount( redisService.getViewCount(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
                case "Guide" -> {
                    guideService.increaseGuideViewCount(redisService.getViewCount(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
                case "Program" -> {
                    programService.increaseProgramViewCount(redisService.getViewCount(key), Long.parseLong(splitKey[2]));
                    redisService.deleteData(key);
                }
            }
        }
    }

    /**
     * @Description
     * 하루마다 운영상태를 전환합니다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void updateOperateStatus() {
        programService.settingProgramStatus();
        boothService.settingBoothStatus();
    }
}
