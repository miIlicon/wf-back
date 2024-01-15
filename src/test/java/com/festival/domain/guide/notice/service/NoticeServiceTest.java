package com.festival.domain.guide.notice.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.guide.notice.dto.NoticeRes;
import com.festival.domain.guide.notice.model.Notice;
import com.festival.domain.guide.notice.repository.NoticeRepository;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.fixture.MemberFixture;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.viewcount.util.ViewCountUtil;
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

import static com.festival.domain.guide.notice.fixture.GuideFixture.DELETED_NOTICE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {
    
    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private ImageService imageService;

    @Mock
    private ViewCountUtil viewCountUtil;

    @DisplayName("존재하지 않는 가이드를 업데이트하면 NotFoundException을 반환한다.")
    @Test
    void updateNotExistGuide() {
        //given
        NoticeReq guideUpdateReq = getGuideUpdateReq();


        given(noticeRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> noticeService.updateGuide(1L, guideUpdateReq))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_GUIDE);
    }

    @DisplayName("삭제된 가이드를 업데이트하면 AlreadyDeletedException을 반환한다.")
    @Test
    void updateDeletedGuide() throws IOException {
        //given
        NoticeReq guideUpdateReq = getGuideUpdateReq();

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(DELETED_NOTICE));

        //when & then
        assertThatThrownBy(() -> noticeService.updateGuide(1L, guideUpdateReq))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }

    @DisplayName("다른 사람이 가이드를 업데이트하면 ForbiddenException을 반환한다.")
    @Test
    void updateGuide2() throws IOException {
        //given
        NoticeReq guideUpdateReq = getGuideUpdateReq();
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.MANAGER1);

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> noticeService.updateGuide(1L, guideUpdateReq))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_UPDATE);
    }

    @DisplayName("Admin이 가이드를 업데이트하면 guideId를 반환한다.")
    @Test
    void updateGuide3() throws IOException {
        //given
        NoticeReq guideUpdateReq = getGuideUpdateReq();
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.MANAGER1);

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long guideId = noticeService.updateGuide(1L, guideUpdateReq);

        //then
        Assertions.assertThat(guideId).isEqualTo(notice.getId());
    }

    @DisplayName("가이드의 관리자가 업데이트하면 guideId를 반환한다.")
    @Test
    void updateGuide4() throws IOException {
        //given
        NoticeReq guideUpdateReq = getGuideUpdateReq();
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.ADMIN);

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when
        Long guideId = noticeService.updateGuide(1L, guideUpdateReq);

        //then
        Assertions.assertThat(guideId).isEqualTo(notice.getId());
    }

    @DisplayName("존재하지 않는 가이드를 삭제하면 NotFoundException을 반환한다.")
    @Test
    void deleteNotExistGuide() {
        //given
        given(noticeRepository.findById(1L))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> noticeService.deleteGuide(1L))
                .isInstanceOf(NotFoundException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.NOT_FOUND_GUIDE);
    }

    @DisplayName("삭제된 가이드를 삭제하면 AlreadyDeletedException을 반환한다.")
    @Test
    void deleteDeletedGuide() {
        //given
        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(DELETED_NOTICE));

        //when & then
        assertThatThrownBy(() -> noticeService.deleteGuide(1L))
                .isInstanceOf(AlreadyDeleteException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ALREADY_DELETED);

    }

    @DisplayName("다른 사람이 가이드를 삭제하면 ForbiddenException을 반환한다.")
    @Test
    void deleteGuide2() throws IOException {
        //given
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.MANAGER1);

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.MANAGER2);

        //when & then
        assertThatThrownBy(() -> noticeService.deleteGuide(1L))
                .isInstanceOf(ForbiddenException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FORBIDDEN_DELETE);
    }

    @DisplayName("Admin이 가이드를 삭제하면 정상처리")
    @Test
    void deleteGuide3() {
        //given
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.MANAGER1);

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when && then
        noticeService.deleteGuide(1L);
    }

    @DisplayName("가이드의 관리자가 업데이트하면 정상동작")
    @Test
    void deleteGuide4(){
        //given
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.ADMIN);

        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        given(memberService.getAuthenticationMember())
                .willReturn(MemberFixture.ADMIN);

        //when && then
        noticeService.deleteGuide(1L);
    }

    @DisplayName("아이디에 맞는 가이드를 반환한다.")
    @Test
    void getguideTest(){
        //given
        Notice notice = getGuide();
        notice.connectMember(MemberFixture.ADMIN);
        given(noticeRepository.findById(1L))
                .willReturn(Optional.of(notice));
        //when
        NoticeRes noticeRes = noticeService.getGuide(1L, "");

        //then
        Assertions.assertThat(noticeRes).usingRecursiveComparison()
                .isEqualTo(NoticeRes.of(notice));
    }
    
    private Notice getGuide(){
        Notice notice = Notice.builder()
                .content("가이드 내용")
                .build();
        ReflectionTestUtils.setField(notice, "id", 1L);
        return notice;
    }
    
    private NoticeReq getGuideCreateReq(){
        return NoticeReq.builder()
                .content("가이드 내용")
                .build();
    }

    private NoticeReq getGuideUpdateReq(){
        return NoticeReq.builder()
                .content("가이드 내용 수정")
                .build();
    }
    
}