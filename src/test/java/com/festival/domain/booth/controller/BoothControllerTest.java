package com.festival.domain.booth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
import com.festival.domain.util.ControllerTestSupport;
import com.festival.domain.util.TestImageUtils;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;

import static com.festival.domain.member.model.MemberRole.ADMIN;
import static com.festival.domain.member.model.MemberRole.MANAGER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoothControllerTest extends ControllerTestSupport {

    @Autowired
    private BoothRepository boothRepository;

    private Member member;

    private Member differentMember;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .username("testUser")
                .password("12345")
                .memberRole(ADMIN)
                .build();
        memberRepository.saveAndFlush(member);

        differentMember = Member.builder()
                .username("differentUser")
                .password("12345")
                .memberRole(MANAGER)
                .build();
        memberRepository.saveAndFlush(differentMember);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("축제부스 게시물을 생성한다.")
    @Test
    void createBooth() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .mainFile(mainFile)
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(
                        multipart("/api/v2/booth")
                                .file(mainFile)
                                .file(subFiles.get(0))
                                .file(subFiles.get(1))
                                .file(subFiles.get(2))
                                .param("title", boothReq.getTitle())
                                .param("subTitle", boothReq.getSubTitle())
                                .param("content", boothReq.getContent())
                                .param("latitude", String.valueOf(boothReq.getLatitude()))
                                .param("longitude", String.valueOf(boothReq.getLongitude()))
                                .param("status", boothReq.getStatus())
                                .param("type", boothReq.getType())
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        Long boothId = Long.parseLong(mvcResult.getResponse().getContentAsString());
        Booth booth = boothRepository.findById(boothId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BOOTH));
        assertThat(booth).isNotNull()
                .extracting("title", "content", "type", "status")
                .containsExactly(boothReq.getTitle(), boothReq.getContent(), BoothType.FOOD_TRUCK, OperateStatus.OPERATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("축제부스 게시물을 수정한다.")
    @Test
    void updateBooth() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .mainFile(mainFile)
                .build();
        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when
        MockHttpServletRequestBuilder builder = multipart("/api/v2/booth/" + savedBooth.getId())
                .file(mainFile)
                .file(subFiles.get(0))
                .file(subFiles.get(1))
                .file(subFiles.get(2))
                .param("title", "updateTitle")
                .param("subTitle", "updateSubTitle")
                .param("content", "updateContent")
                .param("latitude", String.valueOf(50.0f))
                .param("longitude", String.valueOf(50.0f))
                .param("type", "FOOD_TRUCK")
                .param("status", "OPERATE")
                .contentType(MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        MvcResult mvcResult = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(savedBooth.getId().toString()))
                .andReturn();

        //then
        Long boothId = Long.parseLong(mvcResult.getResponse().getContentAsString());
        Booth findBooth = boothRepository.findById(boothId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BOOTH));
        assertThat(findBooth).isNotNull()
                .extracting("title", "content", "type", "status")
                .containsExactly("updateTitle", "updateContent", BoothType.FOOD_TRUCK, OperateStatus.OPERATE);
    }

    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("축제부스 게시물을 수정할 때, 다른 사용자는 수정할 권한이 없다.")
    @Test
    void updateBoothNotMine() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .mainFile(mainFile)
                .build();
        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when //then
        MockHttpServletRequestBuilder builder = multipart("/api/v2/booth/" + savedBooth.getId())
                .file(mainFile)
                .file(subFiles.get(0))
                .file(subFiles.get(1))
                .file(subFiles.get(2))
                .param("title", "updateTitle")
                .param("subTitle", "updateSubTitle")
                .param("content", "updateContent")
                .param("latitude", String.valueOf(50.0f))
                .param("longitude", String.valueOf(50.0f))
                .param("type", "FLEA_MARKET")
                .param("status", "OPERATE")
                .contentType(MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("축제부스 게시물을 수정할 때, 존재하지 않는 게시물은 수정할 수 없다.")
    @Test
    void updateBoothNotFound() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        //when //then
        MockHttpServletRequestBuilder builder = multipart("/api/v2/booth/1")
                .file(mainFile)
                .file(subFiles.get(0))
                .file(subFiles.get(1))
                .file(subFiles.get(2))
                .param("title", "updateTitle")
                .param("subTitle", "updateSubTitle")
                .param("content", "updateContent")
                .param("latitude", String.valueOf(50.0f))
                .param("longitude", String.valueOf(50.0f))
                .param("type", "FLEA_MARKET")
                .param("status", "OPERATE")
                .contentType(MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                });

        mockMvc.perform(builder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("축제부스 게시물을 하나 삭제한다.")
    @Test
    void deleteBooth() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .mainFile(mainFile)
                .build();
        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when
        mockMvc.perform(
                        delete("/api/v2/booth/" + savedBooth.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Booth findBooth = boothRepository.findById(savedBooth.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BOOTH));
        assertThat(findBooth.getStatus()).isEqualTo(OperateStatus.TERMINATE);
    }

    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("삭제권한이 없는 게시물은 삭제할 수 없다.")
    @Test
    void deleteBoothNotMine() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .mainFile(mainFile)
                .build();
        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when //then
        mockMvc.perform(
                        delete("/api/v2/booth/" + savedBooth.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("존재하지 않는 축제부스 게시물은 삭제할 수 없다.")
    @Test
    void deleteBoothNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        delete("/api/v2/booth/1")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("축제부스 게시물 중에 하나를 조회한다.")
    @Test
    void getBooth() throws Exception {
        //given
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .build();
        Booth booth = Booth.of(boothReq);
        Image image = Image.builder()
                .mainFilePath(mainFile.getName())
                .subFilePaths(subFiles.stream().map(MockMultipartFile::getName).toList())
                .build();
        booth.connectMember(member);
        booth.setImage(image);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/booth/" + savedBooth.getId())
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        BoothRes findBooth = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), BoothRes.class);
        assertThat(findBooth).isNotNull()
                .extracting("title", "content", "type", "status")
                .containsExactly(boothReq.getTitle(), boothReq.getContent(), "FOOD_TRUCK", "OPERATE");
    }

    @DisplayName("존재하지 않는 축제부스 게시물은 조회할 수 없다.")
    @Test
    void getBoothNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        get("/api/v2/booth/1")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("축제부스 게시물을 목록조회한다. (페이징)")
    @Test
    void getBoothList() throws Exception {
        //given
        createBoothEntity(1);
        createBoothEntity(2);
        createBoothEntity(3);
        createBoothEntity(4);
        createBoothEntity(5);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/booth/list")
                                .param("status", "OPERATE")
                                .param("type", "FOOD_TRUCK")
                                .param("page", pageable.getPageNumber() + "")
                                .param("size", pageable.getPageSize() + "")
                                .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String content = mvcResult.getResponse().getContentAsString();
        List<BoothRes> boothResList = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, BoothRes.class));
        assertThat(boothResList).hasSize(5)
                .extracting("title", "content", "type", "status")
                .containsExactlyInAnyOrder(
                        tuple("testTitle1", "testContent1", "FOOD_TRUCK", "OPERATE"),
                        tuple("testTitle2", "testContent2", "FOOD_TRUCK", "OPERATE"),
                        tuple("testTitle3", "testContent3", "FOOD_TRUCK", "OPERATE"),
                        tuple("testTitle4", "testContent4", "FOOD_TRUCK", "OPERATE"),
                        tuple("testTitle5", "testContent5", "FOOD_TRUCK", "OPERATE")
                );
    }

    private Booth createBoothEntity(int count) throws IOException {
        MockMultipartFile mainFile = TestImageUtils.generateMockImageFile("mainFile");
        List<MockMultipartFile> subFiles = List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );

        BoothReq boothReq = BoothReq.builder()
                .title("testTitle" + count)
                .subTitle("testSubTitle" + count)
                .content("testContent" + count)
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .status("OPERATE")
                .build();
        Booth booth = Booth.of(boothReq);
        Image image = Image.builder()
                .mainFilePath(mainFile.getName())
                .subFilePaths(subFiles.stream().map(MockMultipartFile::getName).toList())
                .build();
        booth.connectMember(member);
        booth.setImage(image);
        return boothRepository.saveAndFlush(booth);
    }
}