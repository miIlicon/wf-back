package com.festival.common.data;

import com.festival.domain.booth.repository.BoothJdbcRepository;
import com.festival.domain.image.repository.ImageRepository;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.viewcount.repository.HomeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev"})
@Configuration
@Transactional
public class Data {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ImageRepository imageRepository,
            BoothJdbcRepository boothJdbcRepository,
            HomeRepository homeRepository
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;
            long start = System.currentTimeMillis();
        };
    }
}
