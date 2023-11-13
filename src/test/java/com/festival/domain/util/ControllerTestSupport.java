package com.festival.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.common.redis.RedisService;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.viewcount.util.ViewCountUtil;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RedisService redisService;

    @Autowired
    protected ViewCountUtil viewCountUtil;

}
