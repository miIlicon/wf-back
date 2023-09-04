package com.festival.domain.bambooforest.controller;

import com.festival.common.base.OperateStatus;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.member.model.Member;
import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.festival.domain.member.model.MemberRole.ADMIN;
import static com.festival.domain.member.model.MemberRole.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BambooForestIntegrationTest extends ControllerTestSupport {

    @Autowired
    private BamBooForestRepository bamBooForestRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testUser")
                .password("12345")
                .memberRole(ADMIN)
                .build();
        memberRepository.saveAndFlush(member);

        Member differentMember = Member.builder()
                .username("differentUser")
                .password("12345")
                .memberRole(MANAGER)
                .build();
        memberRepository.saveAndFlush(differentMember);
    }

    @DisplayName("대나무숲에 글을 하나 작성한다.")
    @Test
    void createBamBooForest() throws Exception {
        //given
        BamBooForestReq request = createBamBooForestReq("bambooForestContent", "festival@email.com", "OPERATE");

        //when //then
        mockMvc.perform(
                        post("/api/v2/bambooforest")
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("content", request.getContent())
                                .param("contact", request.getContact())
                                .param("status", request.getStatus())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("대나무 숲에 적힌 글들 중 하나를 선택하여 삭제한다.")
    @Test
    void deleteBamBooForest() throws Exception {
        //given
        BamBooForestReq request = createBamBooForestReq("bambooForestContent", "festival@email.com", "OPERATE");
        BamBooForest bamBooForest = BamBooForest.of(request);
        BamBooForest savedBamBooForest = bamBooForestRepository.saveAndFlush(bamBooForest);

        //when
        mockMvc.perform(
                        delete("/api/v2/bambooforest/" + savedBamBooForest.getId())
                                .contentType(APPLICATION_FORM_URLENCODED)
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        BamBooForest findBambooForest = bamBooForestRepository.findById(savedBamBooForest.getId()).get();
        assertThat(findBambooForest.getStatus()).isEqualTo(OperateStatus.TERMINATE);
    }

    @DisplayName("대나무숲 게시물이 존재하지 않으면, NotFoundException을 발생시킨다.")
    @Test
    void deleteBambooNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        delete("/api/v2/bambooforest/1")
                                .contentType(APPLICATION_FORM_URLENCODED)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("대나무숲의 게시물은 10개씩 표시된다. 데이터는 페이지 처리된다.")
    @Test
    void getListBamBooForest() throws Exception {
        //given
        BamBooForestReq request1 = createBamBooForestReq("bamboo1", "festival1@email.com", "OPERATE");
        BamBooForestReq request2 = createBamBooForestReq("bamboo2", "festival2@email.com", "OPERATE");
        BamBooForestReq request3 = createBamBooForestReq("bamboo3", "festival3@email.com", "OPERATE");
        BamBooForestReq request4 = createBamBooForestReq("bamboo4", "festival4@email.com", "OPERATE");
        BamBooForestReq request5 = createBamBooForestReq("bamboo5", "festival5@email.com", "OPERATE");
        BamBooForestReq request6 = createBamBooForestReq("bamboo6", "festival6@email.com", "OPERATE");
        BamBooForestReq request7 = createBamBooForestReq("bamboo7", "festival7@email.com", "OPERATE");
        BamBooForestReq request8 = createBamBooForestReq("bamboo8", "festival8@email.com", "OPERATE");
        BamBooForestReq request9 = createBamBooForestReq("bamboo9", "festival9@email.com", "OPERATE");
        BamBooForestReq request10 = createBamBooForestReq("bamboo10", "festival10@email.com", "OPERATE");
        BamBooForestReq request11 = createBamBooForestReq("bamboo11", "festival11@email.com", "OPERATE");

        BamBooForest bamBooForest1 = BamBooForest.of(request1);
        BamBooForest bamBooForest2 = BamBooForest.of(request2);
        BamBooForest bamBooForest3 = BamBooForest.of(request3);
        BamBooForest bamBooForest4 = BamBooForest.of(request4);
        BamBooForest bamBooForest5 = BamBooForest.of(request5);
        BamBooForest bamBooForest6 = BamBooForest.of(request6);
        BamBooForest bamBooForest7 = BamBooForest.of(request7);
        BamBooForest bamBooForest8 = BamBooForest.of(request8);
        BamBooForest bamBooForest9 = BamBooForest.of(request9);
        BamBooForest bamBooForest10 = BamBooForest.of(request10);
        BamBooForest bamBooForest11 = BamBooForest.of(request11);

        List<BamBooForest> bamBooForests = bamBooForestRepository.saveAllAndFlush(List.of(
                bamBooForest1, bamBooForest2, bamBooForest3,
                bamBooForest4, bamBooForest5, bamBooForest6,
                bamBooForest7, bamBooForest8, bamBooForest9,
                bamBooForest10, bamBooForest11
        ));

        //when //then
        String status = "OPERATE";
        Pageable pageable = PageRequest.of(0, 10);

        mockMvc.perform(
                        get("/api/v2/bambooforest/list")
                                .contentType(APPLICATION_FORM_URLENCODED)
                                .param("status", status)
                                .param("page", String.valueOf(pageable.getPageNumber()))
                                .param("size", String.valueOf(pageable.getPageSize()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(10)));
    }

    private static BamBooForestReq createBamBooForestReq(String bambooForestContent, String mail, String operate) {
        return BamBooForestReq.builder()
                .content(bambooForestContent)
                .contact(mail)
                .status(operate)
                .build();
    }
}