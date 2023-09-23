package com.festival.common.data;

import com.festival.domain.booth.controller.dto.BulkInsertBooth;
import com.festival.domain.booth.repository.BoothJdbcRepository;
import com.festival.domain.image.model.Image;
import com.festival.domain.image.repository.ImageRepository;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.service.MemberService;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Profile({"dev", "prod"})
@Configuration
@Transactional
public class Data {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ImageRepository imageRepository,
            BoothJdbcRepository boothJdbcRepository
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;
            Long memberId = memberService.join(MemberJoinReq.builder()
                    .username("user")
                    .password("1234")
                    .memberRole("ADMIN")
                    .build());
            Long imageId = imageRepository.save(Image.builder()
                    .mainFilePath("")
                    .subFilePaths(List.of("")).build()).getId();

            int cnt = 1;
            long start = System.currentTimeMillis();
            
            for(int i = 0; i < 1; i++){
                List<BulkInsertBooth> boothList = new ArrayList<>();
                for(int j = 0;j < 1000; j++){

                    boothList.add(BulkInsertBooth.builder()
                            .title("testTitle" + cnt)
                            .status("OPERATE")
                            .subTitle("testSubTitle" + cnt)
                            .content("testContent" + cnt)
                            .longitude(50L)
                            .latitude(50L)
                            .type("PUB")
                            .imageId(imageId)
                            .memberId(memberId)
                            .lastModifiedBy("user")
                            .createdBy("user")
                            .build());
                    cnt += 1;
                }
                boothJdbcRepository.insertBoothList(boothList);
            }

            long executionTime = System.currentTimeMillis() - start;
            System.out.println("===================== BulkInsert Success =====================");
            System.out.println("수행 시간 : " + executionTime + " ms");
        };
    }
}
