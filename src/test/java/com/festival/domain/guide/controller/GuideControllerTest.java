package com.festival.domain.guide.controller;

import com.festival.common.base.OperateStatus;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
import com.festival.domain.image.fixture.ImageFixture;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.util.ControllerTestSupport;
import com.festival.domain.util.TestImageUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.festival.domain.guide.model.GuideType.NOTICE;
import static com.festival.domain.guide.model.GuideType.QNA;
import static com.festival.domain.member.model.MemberRole.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GuideControllerTest extends ControllerTestSupport {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testUser")
                .password("12345")
                .memberRole(ADMIN)
                .build();
        memberRepository.saveAndFlush(member);

        member = Member.builder()
                .username("differentUser")
                .password("12345")
                .memberRole(ADMIN)
                .build();
        memberRepository.saveAndFlush(member);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물을 등록한다.")
    @Test
    void createGuide() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        GuideReq guideReq = GuideReq.builder()
                .title("title")
                .content("content")
                .type("QNA")
                .status("OPERATE")
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(
                        multipart("/api/v2/guide")
                                .file(mainFile)
                                .file(subFiles.get(0))
                                .file(subFiles.get(1))
                                .file(subFiles.get(2))
                                .param("title", guideReq.getTitle())
                                .param("content", guideReq.getContent())
                                .param("type", guideReq.getType())
                                .param("status", guideReq.getStatus())
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        Long id = objectMapper.readValue(content, Long.class);
        Guide findGuide = guideRepository.findById(id).get();
        assertThat(findGuide).isNotNull()
                .extracting("title", "content", "guideType", "guideStatus")
                .contains("title", "content", QNA, OperateStatus.OPERATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물들의 내용 등을 수정한다.")
    @Test
    void updateGuide() throws Exception {
        //given
        GuideReq guideReq = GuideReq.builder()
                .title("title")
                .content("content")
                .type("QNA")
                .status("OPERATE")
                .build();
        Guide guide = Guide.of(guideReq);
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when
        mockMvc.perform(
                        put("/api/v2/guide/" + savedGuide.getId())
                                .param("title", "updateTitle")
                                .param("content", "updateContent")
                                .param("type", "NOTICE")
                                .param("status", "OPERATE")
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(savedGuide.getId().toString()));

        //then
        Guide findGuide = guideRepository.findById(savedGuide.getId()).get();
        assertThat(findGuide).isNotNull()
                .extracting("title", "content", "guideType", "guideStatus")
                .contains("updateTitle", "updateContent", NOTICE, OperateStatus.OPERATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물중 하나를 선택하여 삭제한다.")
    @Test
    void deleteGuide() throws Exception {
        //given
        Guide guide = createGuideEntity("title", "content", "QNA", "OPERATE");
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when
        mockMvc.perform(
                        delete("/api/v2/guide/" + savedGuide.getId())
                                .contentType(APPLICATION_FORM_URLENCODED)
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        Guide findGuide = guideRepository.findById(savedGuide.getId()).get();
        assertThat(findGuide.getStatus()).isEqualTo(OperateStatus.TERMINATE);
    }

    @WithMockUser(username = "differentUser", roles = "ADMIN")
    @DisplayName("안내사항 게시물을 삭제할 때, 다른 사람은 삭제할 수 없다.")
    @Test
    void NotDeleteDifferentUser() throws Exception {
        //given
        Guide guide = createGuideEntity("title", "content", "QNA", "OPERATE");
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when //then
        mockMvc.perform(
                        delete("/api/v2/guide/" + savedGuide.getId())
                                .contentType(APPLICATION_FORM_URLENCODED)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

//    @DisplayName("안내사항 게시물을 선택했을때, 해당 게시물이 존재하지 않으면 NotFoundException을 반환한다.")
//    @Test
//    void getNullGuide() throws Exception {
//        //when //then
//        mockMvc.perform(
//                        get("/api/v2/guide/1")
//                                .contentType(APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is5xxServerError());
//    }

    @DisplayName("안내사항 게시물 하나를 선택하여 상세 내용을 가져온다.")
    @Test
    void getGuide() throws Exception {
        //given
        Guide guide = createGuideEntity("title", "content", "QNA", "OPERATE");
        Image image = Image.builder()
                .mainFilePath("/mainFile213")
                .subFilePaths(List.of("/subFile1123", "/subFile2123"))
                .build();
        guide.setImage(image);
        guide.connectMember(member);
        Guide savedGuide = guideRepository.saveAndFlush(guide);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/guide/" + savedGuide.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        GuideRes guideRes = objectMapper.readValue(content, GuideRes.class);
        assertThat(guideRes).isNotNull()
                .extracting("title", "content", "type")
                .contains("title", "content", "QNA"
        );
    }

    @DisplayName("안내사항 목록을 가져온다. 목록은 페이징으로 10개씩 처리된다. 테스트 환경에서는 5개의 데이터를 페이징하여 테스트한다.")
    @Test
    void getListGuide() throws Exception {
        //given
        String status = "OPERATE";
        Pageable pageable = PageRequest.of(0, 5);

        Guide guide1 = createGuideEntity("title1", "content1", "QNA", "OPERATE");
        guide1.setImage(ImageFixture.image);
        Guide guide2 = createGuideEntity("title2", "content2", "NOTICE", "OPERATE");
        guide2.setImage(ImageFixture.image);
        Guide guide3 = createGuideEntity("title3", "content3", "QNA", "OPERATE");
        guide3.setImage(ImageFixture.image);
        Guide guide4 = createGuideEntity("title4", "content4", "NOTICE", "OPERATE");
        guide4.setImage(ImageFixture.image);
        Guide guide5 = createGuideEntity("title5", "content5", "QNA", "OPERATE");
        guide5.setImage(ImageFixture.image);
        Guide guide6 = createGuideEntity("title6", "content6", "NOTICE", "OPERATE");
        guide6.setImage(ImageFixture.image);
        Guide guide7 = createGuideEntity("title7", "content7", "QNA", "OPERATE");
        guide7.setImage(ImageFixture.image);
        Guide guide8 = createGuideEntity("title8", "content8", "NOTICE", "OPERATE");
        guide8.setImage(ImageFixture.image);

        List<Guide> guides = guideRepository.saveAllAndFlush(List.of(guide1, guide2, guide3, guide4, guide5, guide6, guide7, guide8));

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/guide/list")
                                .param("status", status)
                                .param("page", String.valueOf(pageable.getPageNumber()))
                                .param("size", String.valueOf(pageable.getPageSize()))
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        List<GuideRes> guideResList = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, GuideRes.class));
        assertThat(guideResList).hasSize(5)
                .extracting("title", "content", "type")
                .containsExactlyInAnyOrder(
                        tuple("title1", "content1", "QNA"),
                        tuple("title2", "content2", "NOTICE"),
                        tuple("title3", "content3", "QNA"),
                        tuple("title4", "content4", "NOTICE"),
                        tuple("title5", "content5", "QNA")
                );
    }

    private Guide createGuideEntity(String title, String content, String type, String status) {
        GuideReq guideReq = GuideReq.builder()
                .title(title)
                .content(content)
                .type(type)
                .status(status)
                .build();
        Guide guide = Guide.of(guideReq);
        guide.connectMember(member);
        return guide;
    }
}