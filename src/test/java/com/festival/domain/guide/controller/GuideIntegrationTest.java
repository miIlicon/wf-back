package com.festival.domain.guide.controller;

import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
import com.festival.domain.member.model.Member;
import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.festival.domain.member.model.MemberRole.ADMIN;
import static com.festival.domain.member.model.MemberRole.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GuideIntegrationTest extends ControllerTestSupport {

    @Autowired
    private GuideRepository guideRepository;

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

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물을 등록한다.")
    @Test
    void createGuide() throws Exception {
        //given
        GuideReq guideReq = GuideReq.builder()
                .content("content")
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v2/guide")
                                .param("content", guideReq.getContent())
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        Long id = objectMapper.readValue(content, Long.class);
        Guide findGuide = guideRepository.findById(id).get();
        assertThat(findGuide).isNotNull();
        assertThat(findGuide.getContent()).isEqualTo(guideReq.getContent());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물들의 내용 등을 수정한다.")
    @Test
    void updateGuide() throws Exception {
        //given
        GuideReq guideReq = GuideReq.builder()
                .content("content")
                .build();
        Guide guide = Guide.of(guideReq);
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when
        String updateContent = "updateContent";
        mockMvc.perform(
                put("/api/v2/guide/" + savedGuide.getId())
                        .param("content", updateContent)
                        .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(savedGuide.getId().toString()));

        //then
        Guide findGuide = guideRepository.findById(savedGuide.getId()).get();
        assertThat(findGuide).isNotNull();
        assertThat(findGuide.getContent()).isEqualTo(updateContent);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("게시물을 수정 시도할 때, 안내사항 게시물이 존재하지 않으면 NotFoundException을 반환한다.")
    @Test
    void updateGuideNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                put("/api/v2/guide/1")
                        .param("content", "updateContent")
                        .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("안내사항 게시물은 다른 사람이 수정할 수 없다.")
    @Test
    void updateGuideNotMine() throws Exception {
        //given
        Guide guide = createGuideEntity("content");
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when //then
        mockMvc.perform(
                put("/api/v2/guide/" + savedGuide.getId())
                        .param("content", "updateContent")
                        .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물중 하나를 선택하여 삭제한다.")
    @Test
    void deleteGuide() throws Exception {
        //given
        Guide guide = createGuideEntity("content");
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when
        mockMvc.perform(
                        delete("/api/v2/guide/" + savedGuide.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        Guide findGuide = guideRepository.findById(savedGuide.getId()).get();
        /**
         *   deleted로 판단해야함
         */

        //assertThat(findGuide.getStatus()).isEqualTo(OperateStatus.TERMINATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물이 존재하지 않는데 삭제를 시도하면 NotFoundException을 반환한다.")
    @Test
    void deleteNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        delete("/api/v2/guide/1")
                )
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("안내사항 게시물을 삭제할 때, 다른 사람은 삭제할 수 없다.")
    @Test
    void NotDeleteDifferentUser() throws Exception {
        //given
        Guide guide = createGuideEntity("content");
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when //then
        mockMvc.perform(
                        delete("/api/v2/guide/" + savedGuide.getId())
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("안내사항 게시물을 선택했을때, 해당 게시물이 존재하지 않으면 NotFoundException을 반환한다.")
    @Test
    void getNullGuide() throws Exception {
        //when //then
        mockMvc.perform(
                        get("/api/v2/guide/1")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("안내사항 게시물 하나를 선택하여 상세 내용을 가져온다.")
    @Test
    void getGuide() throws Exception {
        //given
        Guide guide = createGuideEntity("content");
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/guide/" + savedGuide.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        GuideRes guideRes = objectMapper.readValue(content, GuideRes.class);
        assertThat(guideRes).isNotNull();
        assertThat(guideRes.getContent()).isEqualTo(savedGuide.getContent());
    }

    @DisplayName("안내사항 목록을 가져온다. 목록은 페이징으로 10개씩 처리된다.")
    @Test
    void getListGuide() throws Exception {
        //given
        String status = "OPERATE";
        Pageable pageable = PageRequest.of(0, 10);

        Guide guide1 = createGuideEntity("content1");
        Guide guide2 = createGuideEntity("content2");
        Guide guide3 = createGuideEntity("content3");
        Guide guide4 = createGuideEntity("content4");
        Guide guide5 = createGuideEntity("content5");
        Guide guide6 = createGuideEntity("content6");
        Guide guide7 = createGuideEntity("content7");
        Guide guide8 = createGuideEntity("content8");
        Guide guide9 = createGuideEntity("content9");
        Guide guide10 = createGuideEntity("content10");
        Guide guide11 = createGuideEntity("content11");

        List<Guide> guides = guideRepository.saveAllAndFlush(List.of(
                guide1, guide2, guide3, guide4, guide5, guide6, guide7, guide8,
                guide9, guide10, guide11));

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/guide/list")
                                .param("page", String.valueOf(pageable.getPageNumber()))
                                .param("size", String.valueOf(pageable.getPageSize()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        GuidePageRes guidePageRes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), GuidePageRes.class);
        assertThat(guidePageRes.getGuideResList()).hasSize(10)
                .extracting("content")
                .containsExactlyInAnyOrder(
                        "content11", "content10", "content9", "content8", "content7",
                        "content6", "content5", "content4", "content3", "content2"
                );
        assertThat(guidePageRes).isNotNull()
             .extracting("totalCount", "totalPage", "pageNumber", "pageSize")
             .contains(11L, 2, 0, 10);
    }

    private Guide createGuideEntity(String content ) {
        GuideReq guideReq = GuideReq.builder()
                .content(content)
                .build();
        Guide guide = Guide.of(guideReq);
        guide.connectMember(member);
        return guide;
    }
}