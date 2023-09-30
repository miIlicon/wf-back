package com.festival.domain.bambooforest.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepository;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.repository.MemberRepository;
import com.festival.domain.member.service.MemberService;
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
import static com.festival.domain.bambooforest.fixture.BamBooForestFixture.TERMINATED_BAMBOOFOREST;
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
    @Mock
    private MemberService memberService;

    @DisplayName("대나무 숲을 생성한 후 boothId를 반환한다.")
    @Test
    void createBamBooForest() {

        //given
        BamBooForestReq bambooCreateReq = getBamBooForestCreateReq();

        given(bamBooForestRepository.save(any(BamBooForest.class)))
                .willReturn(getBamBooForest());

        //when
        Long bambooForestId = bamBooForestService.createBamBooForest(bambooCreateReq);

        //then
        Assertions.assertThat(bambooForestId).isEqualTo(1L);
    }



    @DisplayName("대나무 숲을 삭제하면 오류가 없을 시 정상흐름.")
    @Test
    void deleteBamBooForest1() {

        //given
        given(bamBooForestRepository.findById(1L))
                .willReturn(Optional.of(getBamBooForest()));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);
        //when
        bamBooForestService.deleteBamBooForest(1L);


        //then
    }


    @DisplayName("존재하지 않는 대나무 숲을 삭제하면 NotFoundException를 반환한다.")
    @Test
    void deleteNotExistBamBooForest() {

        //given
        given(bamBooForestRepository.findById(1L))
                .willThrow(new NotFoundException(NOT_FOUND_BAMBOO));


        //when & then
        assertThatThrownBy(() -> bamBooForestService.deleteBamBooForest(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(NOT_FOUND_BAMBOO);
    }

    @DisplayName("이미 삭제된 대나무 숲을 삭제하면 AlreadyDeletedException이 발생")
    @Test
    void deleteAlreadyDeletedBamBooForest() {

        //given
        given(bamBooForestRepository.findById(1L))
                .willReturn(Optional.of(TERMINATED_BAMBOOFOREST));

        //when & then
        assertThatThrownBy(() -> bamBooForestService.deleteBamBooForest(1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);
    }

    @DisplayName("권한 없는 사람이 대나무 숲을 삭제하면 ForbiddenException이 발생")
    @Test
    void deleteBamBooForest2() {

        //given
        given(bamBooForestRepository.findById(1L))
                .willReturn(Optional.of(getBamBooForest()));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER1);

        //when & then
        assertThatThrownBy(() -> bamBooForestService.deleteBamBooForest(1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_DELETE);
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
    private BamBooForest getBamBooForest(){
        BamBooForest bamBooForest = BamBooForest.builder()
                .content("대나무 숲 글 내용")
                .contact("010-1234-5678")
                .build();
        ReflectionTestUtils.setField(bamBooForest, "id", 1L);
        return bamBooForest;
    }
    private BamBooForestReq getBamBooForestCreateReq() {
        return BamBooForestReq.builder()
                .content("대나무 숲 글 내용")
                .status("OPERATE")
                .contact("010-1234-5678")
                .build();
    }

}