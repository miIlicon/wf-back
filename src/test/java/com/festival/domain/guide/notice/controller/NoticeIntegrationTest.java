package com.festival.domain.guide.notice.controller;

import com.festival.domain.guide.notice.dto.NoticePageRes;
import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.guide.notice.dto.NoticeRes;
import com.festival.domain.guide.notice.model.Notice;
import com.festival.domain.guide.notice.repository.NoticeRepository;
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

class NoticeIntegrationTest extends ControllerTestSupport {

    @Autowired
    private NoticeRepository noticeRepository;

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
        NoticeReq noticeReq = NoticeReq.builder()
                .content("content")
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v2/guide")
                                .param("content", noticeReq.getContent())
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        Long id = objectMapper.readValue(content, Long.class);
        Notice findNotice = noticeRepository.findById(id).get();
        assertThat(findNotice).isNotNull();
        assertThat(findNotice.getContent()).isEqualTo(noticeReq.getContent());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물들의 내용 등을 수정한다.")
    @Test
    void updateGuide() throws Exception {
        //given
        NoticeReq noticeReq = NoticeReq.builder()
                .content("content")
                .build();
        Notice notice = Notice.of(noticeReq);
        notice.connectMember(member);
        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        //when
        String updateContent = "updateContent";
        mockMvc.perform(
                put("/api/v2/guide/" + savedNotice.getId())
                        .param("content", updateContent)
                        .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(savedNotice.getId().toString()));

        //then
        Notice findNotice = noticeRepository.findById(savedNotice.getId()).get();
        assertThat(findNotice).isNotNull();
        assertThat(findNotice.getContent()).isEqualTo(updateContent);
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
        Notice notice = createGuideEntity("content");
        notice.connectMember(member);
        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        //when //then
        mockMvc.perform(
                put("/api/v2/guide/" + savedNotice.getId())
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
        Notice notice = createGuideEntity("content");
        notice.connectMember(member);
        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        //when
        mockMvc.perform(
                        delete("/api/v2/guide/" + savedNotice.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        Notice findNotice = noticeRepository.findById(savedNotice.getId()).get();
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
        Notice notice = createGuideEntity("content");
        notice.connectMember(member);
        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        //when //then
        mockMvc.perform(
                        delete("/api/v2/guide/" + savedNotice.getId())
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
        Notice notice = createGuideEntity("content");
        notice.connectMember(member);
        Notice savedNotice = noticeRepository.saveAndFlush(notice);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/guide/" + savedNotice.getId())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        NoticeRes noticeRes = objectMapper.readValue(content, NoticeRes.class);
        assertThat(noticeRes).isNotNull();
        assertThat(noticeRes.getContent()).isEqualTo(savedNotice.getContent());
    }

    @DisplayName("안내사항 목록을 가져온다. 목록은 페이징으로 10개씩 처리된다.")
    @Test
    void getListGuide() throws Exception {
        //given
        String status = "OPERATE";
        Pageable pageable = PageRequest.of(0, 10);

        Notice notice1 = createGuideEntity("content1");
        Notice notice2 = createGuideEntity("content2");
        Notice notice3 = createGuideEntity("content3");
        Notice notice4 = createGuideEntity("content4");
        Notice notice5 = createGuideEntity("content5");
        Notice notice6 = createGuideEntity("content6");
        Notice notice7 = createGuideEntity("content7");
        Notice notice8 = createGuideEntity("content8");
        Notice notice9 = createGuideEntity("content9");
        Notice notice10 = createGuideEntity("content10");
        Notice notice11 = createGuideEntity("content11");

        List<Notice> notices = noticeRepository.saveAllAndFlush(List.of(
                notice1, notice2, notice3, notice4, notice5, notice6, notice7, notice8,
                notice9, notice10, notice11));

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
        NoticePageRes noticePageRes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), NoticePageRes.class);
        assertThat(noticePageRes.getNoticeResList()).hasSize(10)
                .extracting("content")
                .containsExactlyInAnyOrder(
                        "content11", "content10", "content9", "content8", "content7",
                        "content6", "content5", "content4", "content3", "content2"
                );
        assertThat(noticePageRes).isNotNull()
             .extracting("totalCount", "totalPage", "pageNumber", "pageSize")
             .contains(11L, 2, 0, 10);
    }

    private Notice createGuideEntity(String content ) {
        NoticeReq noticeReq = NoticeReq.builder()
                .content(content)
                .build();
        Notice notice = Notice.of(noticeReq);
        notice.connectMember(member);
        return notice;
    }
}