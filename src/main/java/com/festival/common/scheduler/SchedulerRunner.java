package com.festival.common.scheduler;

import com.festival.common.redis.RedisService;
import com.festival.domain.booth.service.BoothService;
import com.festival.domain.guide.service.GuideService;
import com.festival.domain.program.service.ProgramService;
import com.festival.domain.viewcount.service.ViewCountService;
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
    private final ViewCountService viewCountService;

    /**
     * @Description
     * 설정한 업데이트 주기에 따라 Redis에 쌓여있던 조회수를 RDB에 한번에 반영합니다.
     */
    @Scheduled(fixedDelay = 360000)
    public void updateViewCount()
    {
        Set<String> keySet = redisService.getKeySet("viewCount*");
        for(String key : keySet){
            String[] splitKey = key.split("_"); // 0 : viewCount, 1 : Domain명, 2 : Entity_Id
            switch (splitKey[1]) {
                case "Booth" -> {
                    boothService.increaseBoothViewCount( Long.parseLong(splitKey[2]), redisService.getViewCount(key));
                    redisService.deleteData(key);
                }
                case "Guide" -> {
                    guideService.increaseGuideViewCount(Long.parseLong(splitKey[2]), redisService.getViewCount(key));
                    redisService.deleteData(key);
                }
                case "Program" -> {
                    programService.increaseProgramViewCount(Long.parseLong(splitKey[2]), redisService.getViewCount(key));
                    redisService.deleteData(key);
                }
                case "ViewCount" ->{
                    viewCountService.update(Long.parseLong(splitKey[2]), redisService.getViewCount(key));
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
