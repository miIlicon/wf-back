package com.festival.domain.booth.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.fixture.BoothFixture;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.model.Image;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.festival.domain.booth.fixture.BoothFixture.DELETED_BOOTH;
import static com.festival.domain.util.TestImageUtils.generateMockImageFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoothServiceTest {

    @InjectMocks
    private BoothService boothService;

    @Mock
    private BoothRepository boothRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ImageService imageService;

    @Mock
    private RedisService redisService;

    @DisplayName("부스를 생성한 후 boothId를 반환한다.")
    @Test
    void createBooth() throws IOException {

        //given
        BoothReq boothCreateReq = getBoothCreateReq();

        given(boothRepository.save(any(Booth.class)))
                .willReturn(getBooth());
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when
        Long boothId = boothService.createBooth(boothCreateReq);

        //then
        Assertions.assertThat(boothId).isEqualTo(1L);
    }


    @DisplayName("존재하지 않는 부스를 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateNotExistBooth() throws IOException {
        //given
        BoothReq boothUpdateReq = getBoothUpdateReq();


        given(boothRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> boothService.updateBooth(boothUpdateReq, 1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_BOOTH);

    }
    @DisplayName("삭제된 부스를 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void updateDeletedBooth() throws IOException {
        //given
        BoothReq boothUpdateReq = getBoothUpdateReq();

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(DELETED_BOOTH));

        //when & then
        assertThatThrownBy(() -> boothService.updateBooth(boothUpdateReq, 1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }



    @DisplayName("다른 사람이 부스를 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateBooth2() throws IOException {
        //given
        BoothReq boothUpdateReq = getBoothUpdateReq();
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

    //when & then
    assertThatThrownBy(() -> boothService.updateBooth(boothUpdateReq,1L))
            .isInstanceOf(ForbiddenException.class)
            .extracting("errorCode")
            .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }
    @DisplayName("Admin이 부스를 업데이트하면 boothId를 반환한다.")
    @Test
    void updateBooth3() throws IOException {
        //given
        BoothReq boothUpdateReq = getBoothUpdateReq();
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long boothId = boothService.updateBooth(boothUpdateReq, 1L);

        //then
        Assertions.assertThat(boothId).isEqualTo(booth.getId());
    }
    @DisplayName("부스의 관리자가 업데이트하면 boothId를 반환한다.")
    @Test
    void updateBooth4() throws IOException {
        //given
        BoothReq boothUpdateReq = getBoothUpdateReq();
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when
        Long boothId = boothService.updateBooth(boothUpdateReq, 1L);

        //then
        Assertions.assertThat(boothId).isEqualTo(booth.getId());
    }

    //   ###############################################################

    @DisplayName("삭제된 부스의 상태값을 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void updateBoothOperateStatus() throws IOException {
        //given
        given(boothRepository.findById(1L))
                .willReturn(Optional.of(DELETED_BOOTH));

        //when & then
        assertThatThrownBy(() -> boothService.updateBoothOperateStatus(OperateStatus.OPERATE.getValue(), 1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }



    @DisplayName("다른 사람이 부스 상태값을 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateBoothOperateStatus2() throws IOException {
        //given
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> boothService.updateBoothOperateStatus(OperateStatus.OPERATE.getValue(), 1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }
    @DisplayName("Admin이 부스 상태값을 업데이트하면 boothId를 반환한다.")
    @Test
    void updateBoothOperateStatus3() throws IOException {
        //given
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long boothId = boothService.updateBoothOperateStatus(OperateStatus.OPERATE.getValue(), 1L);

        //then
        Assertions.assertThat(boothId).isEqualTo(booth.getId());
    }
    @DisplayName("부스의 관리자가 업데이트하면 boothId를 반환한다.")
    @Test
    void updateBoothOperateStatus4() throws IOException {
        //given
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when
        Long boothId = boothService.updateBoothOperateStatus(OperateStatus.OPERATE.getValue(), 1L);

        //then
        Assertions.assertThat(boothId).isEqualTo(booth.getId());
    }
    @DisplayName("존재하지 않는 부스의 상태를 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateBoothOperateStatusBooth() {
        //given
        given(boothRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() ->boothService.updateBoothOperateStatus(OperateStatus.OPERATE.getValue(), 1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_BOOTH);

    }

    // #####################################################################

    @DisplayName("존재하지 않는 부스를 삭제하면 NotFoundException을 반환한다.")
    @Test
    void deleteNotExistBooth() {
        //given
        given(boothRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> boothService.deleteBooth(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_BOOTH);

    }
    @DisplayName("삭제된 부스를 삭제하면 AlreadyDeletedException을 반환한다.")
    @Test
    void deleteDeletedBooth() {
        //given
        given(boothRepository.findById(1L))
                .willReturn(Optional.of(DELETED_BOOTH));

        //when & then
        assertThatThrownBy(() -> boothService.deleteBooth(1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }

    @DisplayName("다른 사람이 부스를 삭제하면 ForbiddenException을 반환한다.")
    @Test
    void deleteBooth2() throws IOException {
        //given
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> boothService.deleteBooth(1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_DELETE);
    }
    @DisplayName("Admin이 부스를 삭제하면 정상처리")
    @Test
    void deleteBooth3() {
        //given
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when && then
        boothService.deleteBooth(1L);
    }
    @DisplayName("부스의 관리자가 업데이트하면 정상동작")
    @Test
    void deleteBooth4(){
        //given
        Booth booth = getBooth();
        booth.connectMember(MemberFixture.MANAGER1);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(booth));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when && then
        boothService.deleteBooth(1L);
    }
    @DisplayName("아이디에 맞는 부스를 반환한다.")
    @Test
    void getBoothTest(){
        //given
        Booth foodTruck = BoothFixture.FOOD_TRUCK;
        ReflectionTestUtils.setField(foodTruck, "image", createImage());

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(foodTruck));
        //when
        BoothRes boothRes = boothService.getBooth(1L, "");

        //then
        Assertions.assertThat(boothRes).usingRecursiveComparison()
                .isEqualTo(BoothRes.of(foodTruck));
    }

    @DisplayName("하나의 부스를 등록 후 리스트 조회하면 사이즈는 1")
    @Test
    void getBoothList(){
        //given
        given(boothRepository.getList(any(BoothListSearchCond.class)))
                .willReturn(BoothPageRes.builder()
                        .boothResList(List.of(BoothRes.builder().
                                build())).build());
        //when
        BoothListReq boothListReq = BoothListReq.builder()
                .type("PUB")
                .page(0)
                .size(3)
                .build();

        BoothPageRes boothPageRes = boothService.getBoothList(boothListReq);

        //then
        Assertions.assertThat(boothPageRes.getBoothResList()).hasSize(1);
    }

    private Booth getBooth(){
        Booth booth = Booth.builder()
                .title("부스 게시물 제목")
                .subTitle("부스 게시물 부제목")
                .operateStatus(OperateStatus.OPERATE)
                .content("부스 게시물 내용")
                .longitude(50)
                .latitude(50)
                .type(BoothType.FOOD_TRUCK)
                .build();
        ReflectionTestUtils.setField(booth, "id", 1L);
        return booth;
    }
    private BoothReq getBoothCreateReq() throws IOException {
        BoothReq boothReq = BoothReq.builder()
                .title("푸드트럭 게시물 제목")
                .subTitle("푸드트럭 게시물 부제목")
                .operateStatus("OPERATE")
                .content("푸드트럭 게시물 내용")
                .startDate(LocalDate.of(2023, 9, 15))
                .endDate(LocalDate.of(2023, 9, 16))
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("FOOD_TRUCK")
                .build();
        return boothReq;
    }
    private BoothReq getBoothUpdateReq() throws IOException {
        BoothReq boothReq = BoothReq.builder()
                .title("변경된 제목")
                .subTitle("변경된 부제목")
                .operateStatus("OPERATE")
                .content("변경된 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("FOOD_TRUCK")
                .build();
        return boothReq;
    }

    private Image createImage() {
        return Image.builder()
                .mainFilePath("/mainFile")
                .subFilePaths(List.of("/subFile1", "/subFile2"))
                .build();
    }
}
