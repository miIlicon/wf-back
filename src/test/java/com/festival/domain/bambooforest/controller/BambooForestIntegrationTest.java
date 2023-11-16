package com.festival.domain.bambooforest.controller;

import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.member.model.Member;
import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.festival.domain.member.model.MemberRole.ADMIN;
import static com.festival.domain.member.model.MemberRole.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                                .contentType(MULTIPART_FORM_DATA)
                                .param("content", request.getContent())
                                .param("contact", request.getContact())
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
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        BamBooForest findBambooForest = bamBooForestRepository.findById(savedBamBooForest.getId()).get();
        assertThat(findBambooForest.isDeleted()).isEqualTo(true);
    }

    @DisplayName("대나무숲 게시물이 존재하지 않으면, NotFoundException을 발생시킨다.")
    @WithMockUser(username = "testUser", roles = "ADMIN")
    @Test
    void deleteBambooNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        delete("/api/v2/bambooforest/1")
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("대나무숲 게시물 리스트를 조회한다. 이때 페이징 처리가 되어 10개씩 표시된다. 테스트에서는 3개씩 처리하는 것으로 변경한다.")
    @Test
    void getBamBooForestList() throws Exception {
        //given
        BamBooForest bamBooForest1 = BamBooForest.builder()
                .content("content1")
                .build();
        BamBooForest bamBooForest2 = BamBooForest.builder()
                .content("content2")
                .build();
        BamBooForest bamBooForest3 = BamBooForest.builder()
                .content("content3")
                .build();
        BamBooForest bamBooForest4 = BamBooForest.builder()
                .content("content4")
                .build();
        BamBooForest bamBooForest5 = BamBooForest.builder()
                .content("content5")
                .build();
        bamBooForestRepository.saveAllAndFlush(List.of(bamBooForest1, bamBooForest2, bamBooForest3, bamBooForest4, bamBooForest5));

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/bambooforest/list")
                                .contentType(APPLICATION_JSON_VALUE)
                                .param("page", "0")
                                .param("size", "3")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        BamBooForestPageRes bamBooForestPageRes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BamBooForestPageRes.class);
        assertThat(bamBooForestPageRes.getForestResList()).hasSize(3)
                .extracting("content")
                .containsExactlyInAnyOrder(bamBooForest5.getContent(), bamBooForest4.getContent(), bamBooForest3.getContent());
    }

    private static BamBooForestReq createBamBooForestReq(String bambooForestContent, String mail, String operate) {
        return BamBooForestReq.builder()
                .content(bambooForestContent)
                .contact(mail)
                .build();
    }
}