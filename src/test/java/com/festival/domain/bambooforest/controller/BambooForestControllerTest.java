package com.festival.domain.bambooforest.controller;

import com.festival.common.base.OperateStatus;
import com.festival.domain.bambooforest.dto.BamBooForestCreateReq;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.festival.domain.member.model.MemberRole.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BambooForestControllerTest extends ControllerTestSupport {

    @Autowired
    private BamBooForestRepository bamBooForestRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("대나무숲에 글을 하나 작성한다.")
    @Test
    void createBamBooForest() throws Exception {
        //given
        BamBooForestCreateReq request = createBamBooForestReq("bambooForestContent", "festival@email.com", "OPERATE");

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
        Member member = Member.builder()
                .username("testUser")
                .password("12345")
                .memberRole(ADMIN)
                .build();
        memberRepository.saveAndFlush(member);

        BamBooForestCreateReq request = createBamBooForestReq("bambooForestContent", "festival@email.com", "OPERATE");
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

    @DisplayName("대나무숲의 게시물은 10개씩 표시된다. 데이터는 페이지 처리된다. 테스트 시에는 5개의 데이터를 생성하여 테스트한다.")
    @Test
    void getListBamBooForest() throws Exception {
        //given
        String status = "OPERATE";
        Pageable pageable = PageRequest.of(0, 5);

        BamBooForestCreateReq request1 = createBamBooForestReq("bamboo1", "festival1@email.com", "OPERATE");
        BamBooForestCreateReq request2 = createBamBooForestReq("bamboo2", "festival2@email.com", "OPERATE");
        BamBooForestCreateReq request3 = createBamBooForestReq("bamboo3", "festival3@email.com", "OPERATE");
        BamBooForestCreateReq request4 = createBamBooForestReq("bamboo4", "festival4@email.com", "OPERATE");
        BamBooForestCreateReq request5 = createBamBooForestReq("bamboo5", "festival5@email.com", "OPERATE");
        BamBooForestCreateReq request6 = createBamBooForestReq("bamboo6", "festival6@email.com", "OPERATE");
        BamBooForestCreateReq request7 = createBamBooForestReq("bamboo7", "festival7@email.com", "OPERATE");

        BamBooForest bamBooForest1 = BamBooForest.of(request1);
        BamBooForest bamBooForest2 = BamBooForest.of(request2);
        BamBooForest bamBooForest3 = BamBooForest.of(request3);
        BamBooForest bamBooForest4 = BamBooForest.of(request4);
        BamBooForest bamBooForest5 = BamBooForest.of(request5);
        BamBooForest bamBooForest6 = BamBooForest.of(request6);
        BamBooForest bamBooForest7 = BamBooForest.of(request7);

        List<BamBooForest> bamBooForests = bamBooForestRepository.saveAllAndFlush(List.of(
                bamBooForest1, bamBooForest2, bamBooForest3
                , bamBooForest4, bamBooForest5, bamBooForest6, bamBooForest7)
        );

        //when //then
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
                .andExpect(jsonPath("$.content", hasSize(5)));;
    }

    private static BamBooForestCreateReq createBamBooForestReq(String bambooForestContent, String mail, String operate) {
        return BamBooForestCreateReq.builder()
                .content(bambooForestContent)
                .contact(mail)
                .status(operate)
                .build();
    }
}