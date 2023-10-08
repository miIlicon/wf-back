package com.festival.domain.booth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.BoothService;
import com.festival.domain.image.model.Image;
import com.festival.domain.member.model.Member;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.time.LocalDate;
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

class BoothIntegrationTest extends ControllerTestSupport {

    @Autowired
    private BoothRepository boothRepository;

    private Member member;

    @Autowired
    private BoothService boothService;

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
    @DisplayName("축제부스 게시물을 생성한다.")
    @Test
    void createBooth() throws Exception {
        //given
        MockMultipartFile mainFile = getMainFile();
        List<MockMultipartFile> subFiles = getSubFiles();

        BoothReq boothReq = getBoothReq();

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
                                .param("status", boothReq.getOperateStatus())
                                .param("type", boothReq.getType())
                                .param("startDate", boothReq.getStartDate().toString())
                                .param("endDate", boothReq.getStartDate().toString())
                                .param("operateStatus", "OPERATE")
                                .contentType(MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        Long boothId = Long.parseLong(mvcResult.getResponse().getContentAsString());
        Booth booth = boothRepository.findById(boothId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BOOTH));
        assertThat(booth).isNotNull()
                .extracting("title", "content", "type", "operateStatus")
                .containsExactly(boothReq.getTitle(), boothReq.getContent(), BoothType.FOOD_TRUCK, OperateStatus.TERMINATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("축제부스 게시물을 수정한다.")
    @Test
    void updateBooth() throws Exception {
        //given
        MockMultipartFile mainFile =getMainFile();
        List<MockMultipartFile> subFiles = getSubFiles();

        BoothReq boothReq = getBoothReq();


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
                .param("type", "PUB")
                .param("operateStatus", "OPERATE")
                .param("startDate", boothReq.getStartDate().toString())
                .param("endDate", boothReq.getStartDate().toString())
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
                .extracting("title", "content", "type", "operateStatus")
                .containsExactly("updateTitle", "updateContent", BoothType.PUB, OperateStatus.OPERATE);
    }



    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("축제부스 게시물을 수정할 때, 다른 사용자는 수정할 권한이 없다.")
    @Test
    void updateBoothNotMine() throws Exception {
        //given
        MockMultipartFile mainFile = getMainFile();
        List<MockMultipartFile> subFiles = getSubFiles();

        BoothReq boothReq = getBoothReq();

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
                .param("operateStatus", "OPERATE")
                .param("startDate", boothReq.getStartDate().toString())
                .param("endDate", boothReq.getStartDate().toString())
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
        MockMultipartFile mainFile = getMainFile();
        List<MockMultipartFile> subFiles = getSubFiles();

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
                .param("operateStatus", "OPERATE")
                .param("startDate", "2023-09-15")
                .param("endDate","2023-09-16")
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
    @DisplayName("축제부스 게시물의 운영상태를 변경한다..")
    @Test
    void updateBoothOperateStatus() throws Exception {
        //given
        BoothReq boothReq = getBoothReq();


        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/v2/booth/" + savedBooth.getId())
                            .param("operateStatus", "OPERATE")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        Booth findBooth = boothRepository.findById(Long.parseLong(mvcResult.getResponse().getContentAsString())).orElse(null);
        assertThat(findBooth).isNotNull()
                .extracting("operateStatus")
                .isEqualTo(OperateStatus.OPERATE);
    }

    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("게시물 없으면 상태값을 변경할 수 없다")
    @Test
    void updateBoothOperateStatus2() throws Exception {
        //given

        //when & then
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/v2/booth/" + 1)
                                .param("operateStatus", "OPERATE")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

    }
    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("삭제된 게시물의 상태값은 변경할 수 없다")
    @Test
    void updateBoothOperateStatus3() throws Exception {
        //given
        BoothReq boothReq = getBoothReq();


        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);
        boothService.deleteBooth(savedBooth.getId());

        //when & then
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/v2/booth/" + savedBooth.getId())
                                .param("operateStatus", "OPERATE")
                )
                .andDo(print())
                .andExpect(status().is(400))
                .andReturn();

    }


    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("게시물의 MAMAGER나 ADMIN이 아니면  변경할 수 없다")
    @Test
    void updateBoothOperateStatus4() throws Exception {
        //given
        BoothReq boothReq = getBoothReq();


        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when & then
        MvcResult mvcResult = mockMvc.perform(
                        patch("/api/v2/booth/" + savedBooth.getId())
                                .param("operateStatus", "OPERATE")
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();

    }


    @WithMockUser(username = "testUser", roles = "ADMIN")
    @DisplayName("축제부스 게시물을 하나 삭제한다.")
    @Test
    void deleteBooth() throws Exception {
        //given
        BoothReq boothReq = getBoothReq();

        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when
        mockMvc.perform(
                        delete("/api/v2/booth/" + savedBooth.getId())
                )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Booth findBooth = boothRepository.findById(savedBooth.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_BOOTH));
        assertThat(findBooth.getOperateStatus()).isEqualTo(OperateStatus.TERMINATE);
    }



    @WithMockUser(username = "differentUser", roles = "MANAGER")
    @DisplayName("삭제권한이 없는 게시물은 삭제할 수 없다.")
    @Test
    void deleteBoothNotMine() throws Exception {
        //given
        BoothReq boothReq = getBoothReq();

        Booth booth = Booth.of(boothReq);
        booth.connectMember(member);
        Booth savedBooth = boothRepository.saveAndFlush(booth);

        //when //then
        mockMvc.perform(
                        delete("/api/v2/booth/" + savedBooth.getId())
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("존재하지 않는 축제부스 게시물은 삭제할 수 없다.")
    @WithMockUser(username = "testUser", roles = "ADMIN")
    @Test
    void deleteBoothNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        delete("/api/v2/booth/1")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("축제부스 게시물 중에 하나를 조회한다.")
    @WithMockUser(username = "testUser", roles = "ADMIN")
    @Test
    void getBooth() throws Exception {
        //given
        MockMultipartFile mainFile = getMainFile();
        List<MockMultipartFile> subFiles = getSubFiles();

        BoothReq boothReq = getBoothReq();

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
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        BoothRes findBooth = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), BoothRes.class);
        assertThat(findBooth).isNotNull()
                .extracting("title", "content", "type", "operateStatus")
                .containsExactly(boothReq.getTitle(), boothReq.getContent(), "FOOD_TRUCK", "TERMINATE");
    }

    @DisplayName("존재하지 않는 축제부스 게시물은 조회할 수 없다.")
    @Test
    void getBoothNotFound() throws Exception {
        //when //then
        mockMvc.perform(
                        get("/api/v2/booth/1")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("축제부스 게시물을 목록조회한다. 페이지당 게시물은 6개이다.")
    @Test
    void getBoothList() throws Exception {
        //given
        createBoothEntity(1);
        createBoothEntity(2);
        createBoothEntity(3);
        createBoothEntity(4);
        createBoothEntity(5);
        createBoothEntity(6);


        //when
        Pageable pageable = PageRequest.of(0, 6);

        MvcResult mvcResult = mockMvc.perform(
                        get("/api/v2/booth/list")
                                .param("type", "FOOD_TRUCK")
                                .param("page", pageable.getPageNumber() + "")
                                .param("size", pageable.getPageSize() + "")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        BoothPageRes boothPageRes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BoothPageRes.class);
        assertThat(boothPageRes.getBoothResList()).hasSize(6)
                .extracting("title", "content", "type", "operateStatus")
                .containsExactlyInAnyOrder(
                        tuple("testTitle1", "testContent1", "FOOD_TRUCK", "TERMINATE"),
                        tuple("testTitle2", "testContent2", "FOOD_TRUCK", "TERMINATE"),
                        tuple("testTitle3", "testContent3", "FOOD_TRUCK", "TERMINATE"),
                        tuple("testTitle4", "testContent4", "FOOD_TRUCK", "TERMINATE"),
                        tuple("testTitle5", "testContent5", "FOOD_TRUCK", "TERMINATE"),
                        tuple("testTitle6", "testContent6", "FOOD_TRUCK", "TERMINATE")
                );

        assertThat(boothPageRes).isNotNull()
                .extracting("totalCount", "totalPage", "pageNumber", "pageSize")
                .contains(6L, 1, 0, 6);
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
                .startDate(LocalDate.of(2023, 9, 15))
                .endDate(LocalDate.of(2023, 9, 16))
                .type("FOOD_TRUCK")
                .operateStatus("OPERATE")
                .build();
        Booth booth = Booth.of(boothReq);
        Image image = Image.builder()
                .mainFilePath(mainFile.getName())
                .subFilePaths(subFiles.stream().map(MockMultipartFile::getName).toList())
                .build();
        booth.connectMember(member);
        booth.setImage(image);
        Booth booth11 = booth;
        return boothRepository.saveAndFlush(booth);
    }


    private BoothReq getBoothReq(){
        return BoothReq.builder()
                .title("testTitle")
                .subTitle("testSubTitle")
                .content("testContent")
                .latitude(50.0f)
                .longitude(50.0f)
                .type("FOOD_TRUCK")
                .operateStatus("OPERATE")
                .startDate(LocalDate.of(2023, 9, 14))
                .endDate(LocalDate.of(2023, 9, 15))
                .build();
    }

    private MockMultipartFile getMainFile() throws IOException {
        return TestImageUtils.generateMockImageFile("mainFile");
    }
    private List<MockMultipartFile> getSubFiles() throws IOException {
        return List.of(
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles"),
                TestImageUtils.generateMockImageFile("subFiles")
        );
    }
}