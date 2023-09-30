package com.festival.domain.guide.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepository;
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
import java.util.Optional;

import static com.festival.domain.guide.fixture.GuideFixture.DELETED_GUIDE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GuideServiceTest {
    
    @InjectMocks
    private GuideService guideService;

    @Mock
    private GuideRepository guideRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ImageService imageService;

    @Mock
    private RedisService redisService;

    @DisplayName("존재하지 않는 가이드를 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateNotExistGuide() {
        //given
        GuideReq guideUpdateReq = getGuideUpdateReq();


        given(guideRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> guideService.updateGuide(1L, guideUpdateReq))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_GUIDE);
    }

    @DisplayName("삭제된 가이드를 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void updateDeletedGuide() throws IOException {
        //given
        GuideReq guideUpdateReq = getGuideUpdateReq();

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(DELETED_GUIDE));

        //when & then
        assertThatThrownBy(() -> guideService.updateGuide(1L, guideUpdateReq))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }

    @DisplayName("다른 사람이 가이드를 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateGuide2() throws IOException {
        //given
        GuideReq guideUpdateReq = getGuideUpdateReq();
        Guide guide = getGuide();
        guide.connectMember(MemberFixture.MANAGER1);

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> guideService.updateGuide(1L, guideUpdateReq))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }

    @DisplayName("Admin이 가이드를 업데이트하면 guideId를 반환한다.")
    @Test
    void updateGuide3() throws IOException {
        //given
        GuideReq guideUpdateReq = getGuideUpdateReq();
        Guide guide = getGuide();
        guide.connectMember(MemberFixture.MANAGER1);

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long guideId = guideService.updateGuide(1L, guideUpdateReq);

        //then
        Assertions.assertThat(guideId).isEqualTo(guide.getId());
    }

    @DisplayName("가이드의 관리자가 업데이트하면 guideId를 반환한다.")
    @Test
    void updateGuide4() throws IOException {
        //given
        GuideReq guideUpdateReq = getGuideUpdateReq();
        Guide guide = getGuide();
        guide.connectMember(MemberFixture.ADMIN);

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long guideId = guideService.updateGuide(1L, guideUpdateReq);

        //then
        Assertions.assertThat(guideId).isEqualTo(guide.getId());
    }

    @DisplayName("존재하지 않는 가이드를 삭제하면 NotFoundException을 반환한다.")
    @Test
    void deleteNotExistGuide() {
        //given
        given(guideRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> guideService.deleteGuide(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_GUIDE);
    }

    @DisplayName("삭제된 가이드를 삭제하면 AlreadyDeletedException을 반환한다.")
    @Test
    void deleteDeletedGuide() {
        //given
        given(guideRepository.findById(1L))
                .willReturn(Optional.of(DELETED_GUIDE));

        //when & then
        assertThatThrownBy(() -> guideService.deleteGuide(1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }

    @DisplayName("다른 사람이 가이드를 삭제하면 ForbiddenException을 반환한다.")
    @Test
    void deleteGuide2() throws IOException {
        //given
        Guide guide = getGuide();
        guide.connectMember(MemberFixture.MANAGER1);

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> guideService.deleteGuide(1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_DELETE);
    }

    @DisplayName("Admin이 가이드를 삭제하면 정상처리")
    @Test
    void deleteGuide3() {
        //given
        Guide guide = getGuide();
        guide.connectMember(MemberFixture.MANAGER1);

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when && then
        guideService.deleteGuide(1L);
    }

    @DisplayName("가이드의 관리자가 업데이트하면 정상동작")
    @Test
    void deleteGuide4(){
        //given
        Guide guide = getGuide();
        guide.connectMember(MemberFixture.ADMIN);

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when && then
        guideService.deleteGuide(1L);
    }

    @DisplayName("아이디에 맞는 가이드를 반환한다.")
    @Test
    void getguideTest(){
        //given
        Guide guide = getGuide();

        given(guideRepository.findById(1L))
                .willReturn(Optional.of(guide));
        //when
        GuideRes guideRes = guideService.getGuide(1L, "");

        //then
        Assertions.assertThat(guideRes).usingRecursiveComparison()
                .isEqualTo(GuideRes.of(guide));
    }
    
    private Guide getGuide(){
        Guide guide = Guide.builder()
                .content("가이드 내용")
                .build();
        ReflectionTestUtils.setField(guide, "id", 1L);
        return guide;
    }
    
    private GuideReq getGuideCreateReq(){
        return GuideReq.builder()
                .content("가이드 내용")
                .build();
    }

    private GuideReq getGuideUpdateReq(){
        return GuideReq.builder()
                .content("가이드 내용 수정")
                .build();
    }
    
}