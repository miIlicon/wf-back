package com.festival.domain.viewcount.controller;

import com.festival.common.redis.RedisService;
import com.festival.domain.util.ControllerTestSupport;
import com.festival.domain.viewcount.model.Home;
import com.festival.domain.viewcount.repository.HomeRepository;
import com.festival.domain.viewcount.util.ViewCountUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class HomeControllerTest extends ControllerTestSupport {

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private ViewCountUtil viewCountUtil;

    @Autowired
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        redisService.initialize();
        homeRepository.deleteAllInBatch();
        homeRepository.save(new Home(0));
    }

    @DisplayName("메인 페이지에 접속하면 redis에서 조회수가 1회 증가한다.")
    @Test
    void increaseViewCount() throws Exception {
        //given
        String mockIp = "123.123.123.123";

        //when
        mockMvc.perform(get("/api/v2/view-count")
                        .header("X-FORWARDED-FOR", mockIp)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        assertThat(viewCountUtil.getViewCount("viewCount_Home_1")).isEqualTo(1);
    }

    @DisplayName("조회수는 중복 접속이 아닐 경우에만 증가한다.")
    @Test
    void increaseViewCountNotDuplicate() throws Exception {
        //given
        String mockIp = "123.123.123.123";

        //when
        mockMvc.perform(get("/api/v2/view-count")
                        .header("X-FORWARDED-FOR", mockIp)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v2/view-count")
                        .header("X-FORWARDED-FOR", mockIp)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v2/view-count")
                        .header("X-FORWARDED-FOR", mockIp)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        //then
        assertThat(viewCountUtil.getViewCount("viewCount_Home_1")).isEqualTo(1);
    }
}