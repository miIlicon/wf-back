package com.festival.common.data;

import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile({"dev", "test"})
@Configuration
@Transactional
public class Data {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;
            memberService.join(MemberJoinReq.of("user", "1234", "ADMIN"));
        };
    }
}