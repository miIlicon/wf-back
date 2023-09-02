package com.festival.domain.bambooforest.service;

import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.bambooforest.dto.BamBooForestCreateReq;
import com.festival.domain.bambooforest.fixture.BamBooForestFixture;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.festival.common.exception.ErrorCode.NOT_FOUND_BAMBOO;
import static com.festival.domain.bambooforest.fixture.BamBooForestFixture.bamBooForest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BamBooForestServiceTest {

    @InjectMocks
    private BamBooForestService bamBooForestService;
    @Mock
    private BamBooForestRepository bamBooForestRepository;
    @Mock
    private MemberRepository memberRepository;

    @DisplayName("대나무 숲을 생성한 후 boothId를 반환한다.")
    @Test
    void createBamBooForest() {

        //given
        BamBooForestCreateReq bambooCreateReq = BamBooForestCreateReq.builder()
                .content("대나무 숲 글 내용")
                .status("OPERATE")
                .contact("010-1234-5678")
                .build();

        BamBooForest bamBooForest = BamBooForestFixture.bamBooForest;
        ReflectionTestUtils.setField(bamBooForest, "id", 1L);

        given(bamBooForestRepository.save(any(BamBooForest.class)))
                .willReturn(bamBooForest);

        //when
        Long bambooForestId = bamBooForestService.create(bambooCreateReq);

        //then
        Assertions.assertThat(bambooForestId).isEqualTo(1L);
    }
    @DisplayName("대나무 숲을 삭제하면 오류가 없을 시 정상흐름.")
    @Test
    void deleteBamBooForest1() {

        //given
        given(bamBooForestRepository.findById(1L))
                .willReturn(Optional.of(bamBooForest));
        given(memberRepository.findByUsername(any(String.class)))
                .willReturn(Optional.of(MemberFixture.member1));

        //when
        bamBooForestService.delete(1L);


        //then
    }

    @DisplayName("존재하지 않는 대나무 숲을 삭제하면 NotFoundException를 반환한다.")
    @Test
    void deleteBamBooForest2() {

        //given
        given(bamBooForestRepository.findById(1L))
                .willThrow(new NotFoundException(NOT_FOUND_BAMBOO));


        //when & then
        assertThatThrownBy(() -> bamBooForestService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_BAMBOO);
    }
/*

    @DisplayName("조건에 맞는 대나무숲 리스트 반환한다.")
    @Test
    void getBamBooList(){
        //given
        //when
        //then
    }
*/


}