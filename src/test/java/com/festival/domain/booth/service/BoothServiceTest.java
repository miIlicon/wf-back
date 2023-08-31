package com.festival.domain.booth.service;

import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.fixture.BoothFixture;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.repository.BoothRepository;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.fixture.ImageFixture;
import com.festival.domain.image.service.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.festival.common.exception.ErrorCode.NOT_FOUND_BOOTH;
import static com.festival.domain.util.TestImageUtils.generateMockImageFile;
import static io.jsonwebtoken.lang.Assert.isInstanceOf;
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
    private ImageService imageService;

    @DisplayName("부스를 생성한 후 boothId를 반환한다.")
    @Test
    void createBooth() throws IOException {

        //given
        BoothReq boothReq = BoothReq.builder()
                .title("푸드트럭 게시물 제목")
                .subTitle("푸드트럭 게시물 부제목")
                .status("OPERATE")
                .content("푸드트럭 게시물 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("FOOD_TRUCK")
                .build();

        Booth foodTruck = BoothFixture.FOOD_TRUCK;
        ReflectionTestUtils.setField(foodTruck, "id",1L);

        given(boothRepository.save(any(Booth.class)))
                .willReturn(foodTruck);

        //when
        Long boothId = boothService.createBooth(boothReq);

        //then
        Assertions.assertThat(boothId).isEqualTo(1L);
    }
/*
    @DisplayName("부스를 업데이트하면 boothId를 반환한다.")
    @Test
    void updateBooth() throws IOException {

        //given
        BoothReq boothReq = BoothReq.builder()
                .title("변경된 제목")
                .subTitle("변경된 부제목")
                .status("OPERATE")
                .content("변경된 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("FOOD_TRUCK")
                .build();

        Booth foodTruck = BoothFixture.FOOD_TRUCK;
        ReflectionTestUtils.setField(foodTruck, "id",1L);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(foodTruck));

        //when
        Long boothId = boothService.updateBooth(boothReq,1L, "username");

        //then
        Assertions.assertThat(boothId).isEqualTo(1L);
    }
    @DisplayName("존재하지 않는 부스를 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateNonExistsBooth() throws IOException {

        //given
        BoothReq boothReq = BoothReq.builder()
                .title("변경된 제목")
                .subTitle("변경된 부제목")
                .status("OPERATE")
                .content("변경된 내용")
                .longitude(50)
                .latitude(50)
                .mainFile(generateMockImageFile("mainFile"))
                .subFiles(List.of(generateMockImageFile("subFile1"), generateMockImageFile("subFile1")))
                .type("FOOD_TRUCK")
                .build();


        given(boothRepository.findById(1L))
                .willThrow(new NotFoundException(NOT_FOUND_BOOTH));

        //when & then
        assertThatThrownBy(() -> boothService.updateBooth(boothReq,1L, "username"))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_BOOTH);
    }

    @DisplayName("존재하지 않는 부스를 삭제하면 NotFoundException를 반환한다.")
    @Test
    void updateBooth() throws IOException {

        //given
        given(boothRepository.findById(1L))
                .willThrow(new NotFoundException(NOT_FOUND_BOOTH));


        //when & then
        assertThatThrownBy(() -> boothService.deleteBooth(1L, "username"))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_BOOTH);
    }
*/
    @DisplayName("아이디에 맞는 부스를 반환한다.")
    @Test
    void getBooth(){
        //given
        Booth foodTruck = BoothFixture.FOOD_TRUCK;
        ReflectionTestUtils.setField(foodTruck, "image", ImageFixture.image);

        given(boothRepository.findById(1L))
                .willReturn(Optional.of(foodTruck));
        //when
        BoothRes boothRes = boothService.getBooth(1L);

        //then
        Assertions.assertThat(boothRes).usingRecursiveComparison()
                .isEqualTo(BoothRes.of(foodTruck));
    }

    @DisplayName("조건에 맞는 부스 리스트 반환한다.")
    @Test
    void getBoothList(){
        //given
        Booth pub = BoothFixture.PUB;
        ReflectionTestUtils.setField(pub, "image", ImageFixture.image);
        
        given(boothRepository.getList(any(BoothListSearchCond.class), any(Pageable.class)))
                .willReturn(List.of(pub));
        //when

        BoothListReq boothListReq = BoothListReq.builder()
                .type("PUB")
                .status("OPERATE")
                .build();
        Pageable pageable =  PageRequest.of(0, 3);

        List<BoothRes> boothRes = boothService.getBoothList(boothListReq, pageable);

        //then
        Assertions.assertThat(boothRes).hasSize(1);
    }
}
