package com.festival.domain.guide.notice.repository;

import com.festival.domain.guide.notice.dto.NoticePageRes;
import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.guide.notice.dto.NoticeRes;
import com.festival.domain.guide.notice.model.Notice;
import com.festival.domain.guide.notice.repository.NoticeRepository;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import com.festival.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("dkssud9556")
                .password("1234")
                .memberRole(MemberRole.ADMIN)
                .build();
        savedMember = memberRepository.save(member);
    }

    @DisplayName("안내사항을 생성할 때, 내용을 입력하지 않으면 예외가 발생합니다.")
    @Test
    void createdGuideWithoutContent() throws Exception {
        //given
        NoticeReq noticeReq = NoticeReq.builder()
                .content(null)
                .build();

        //when //then
        assertThatThrownBy(() -> {
            Notice notice = Notice.of(noticeReq);
            noticeRepository.save(notice);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("안내사항 게시물을 조회할 때, 10개씩 페이징되며 조회된다.")
    @Test
    void getList() throws Exception {
        //given
        Notice notice1 = createdGuide("hello1");
        Notice notice2 = createdGuide("hello2");
        Notice notice3 = createdGuide("hello3");
        Notice notice4 = createdGuide("hello4");
        Notice notice5 = createdGuide("hello5");
        Notice notice6 = createdGuide("hello6");
        Notice notice7 = createdGuide("hello7");
        Notice notice8 = createdGuide("hello8");
        Notice notice9 = createdGuide("hello9");
        Notice notice10 = createdGuide("hello10");
        Notice notice11 = createdGuide("hello11");
        noticeRepository.saveAll(List.of(notice1, notice2, notice3,
                notice4, notice5, notice6, notice7, notice8,
                notice9, notice10, notice11));

        //when
        NoticePageRes noticePageRes = noticeRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(noticePageRes.getNoticeResList()).hasSize(10)
                .extracting(NoticeRes::getContent)
                .containsExactly("hello11", "hello10", "hello9", "hello8",
                        "hello7", "hello6", "hello5", "hello4", "hello3", "hello2");
    }

    @DisplayName("데이터를 조회할 때, 페이지 설정이 포함되어서 조회된다.")
    @Test
    void getListWithPageSetting() throws Exception {
        //given
        Notice notice1 = createdGuide("hello1");
        Notice notice2 = createdGuide("hello2");
        Notice notice3 = createdGuide("hello3");
        Notice notice4 = createdGuide("hello4");
        Notice notice5 = createdGuide("hello5");
        Notice notice6 = createdGuide("hello6");
        Notice notice7 = createdGuide("hello7");
        Notice notice8 = createdGuide("hello8");
        Notice notice9 = createdGuide("hello9");
        Notice notice10 = createdGuide("hello10");
        Notice notice11 = createdGuide("hello11");
        noticeRepository.saveAll(List.of(notice1, notice2, notice3,
                notice4, notice5, notice6, notice7, notice8,
                notice9, notice10, notice11));

        //when
        NoticePageRes noticePageRes = noticeRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(noticePageRes.getPageNumber()).isEqualTo(0);
        assertThat(noticePageRes.getPageSize()).isEqualTo(10);
        assertThat(noticePageRes.getTotalPage()).isEqualTo(2);
        assertThat(noticePageRes.getTotalCount()).isEqualTo(11);
    }

    @DisplayName("조회 시, 데이터들의 삭제상태값은 false인 데이터만 조회된다.")
    @Test
    void getListWithoutNotDeleted() throws Exception {
        //given
        Notice notice1 = createdGuide("hello1");
        Notice notice2 = createdGuide("hello2");
        Notice notice3 = createdGuide("hello3");
        notice3.deletedGuide();
        Notice notice4 = createdGuide("hello4");
        Notice notice5 = createdGuide("hello5");
        Notice notice6 = createdGuide("hello6");
        notice6.deletedGuide();
        Notice notice7 = createdGuide("hello7");
        Notice notice8 = createdGuide("hello8");
        Notice notice9 = createdGuide("hello9");
        notice9.deletedGuide();
        Notice notice10 = createdGuide("hello10");
        Notice notice11 = createdGuide("hello11");
        noticeRepository.saveAll(List.of(notice1, notice2, notice3,
                notice4, notice5, notice6, notice7, notice8,
                notice9, notice10, notice11));

        //when
        NoticePageRes noticePageRes = noticeRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(noticePageRes.getNoticeResList()).hasSize(8)
                .extracting(NoticeRes::getContent)
                .containsExactly("hello11", "hello10", "hello8",
                        "hello7", "hello5", "hello4", "hello2", "hello1");
    }

    private Notice createdGuide(String content) {
        Notice notice = Notice.of(NoticeReq.builder().content(content).build());
        notice.connectMember(savedMember);
        return notice;
    }
}