package com.festival.domain.guide.repository;

import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
class GuideRepositoryTest {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("dkssud9556")
                .password("1234")
                .build();
        savedMember = memberRepository.save(member);
    }

    @DisplayName("안내사항을 생성할 때, 내용을 입력하지 않으면 예외가 발생합니다.")
    @Test
    void createdGuideWithoutContent() throws Exception {
        //given
        GuideReq guideReq = GuideReq.builder()
                .content(null)
                .build();

        //when //then
        assertThatThrownBy(() -> {
            Guide guide = Guide.of(guideReq);
            guideRepository.save(guide);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("안내사항 게시물을 조회할 때, 10개씩 페이징되며 조회된다.")
    @Test
    void getList() throws Exception {
        //given
        Guide guide1 = createdGuide("hello1");
        Guide guide2 = createdGuide("hello2");
        Guide guide3 = createdGuide("hello3");
        Guide guide4 = createdGuide("hello4");
        Guide guide5 = createdGuide("hello5");
        Guide guide6 = createdGuide("hello6");
        Guide guide7 = createdGuide("hello7");
        Guide guide8 = createdGuide("hello8");
        Guide guide9 = createdGuide("hello9");
        Guide guide10 = createdGuide("hello10");
        Guide guide11 = createdGuide("hello11");
        guideRepository.saveAll(List.of(guide1, guide2, guide3,
                guide4, guide5, guide6, guide7, guide8,
                guide9, guide10, guide11));

        //when
        GuidePageRes guidePageRes = guideRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(guidePageRes.getGuideResList()).hasSize(10)
                .extracting(GuideRes::getContent)
                .containsExactly("hello11", "hello10", "hello9", "hello8",
                        "hello7", "hello6", "hello5", "hello4", "hello3", "hello2");
    }

    @DisplayName("데이터를 조회할 때, 페이지 설정이 포함되어서 조회된다.")
    @Test
    void getListWithPageSetting() throws Exception {
        //given
        Guide guide1 = createdGuide("hello1");
        Guide guide2 = createdGuide("hello2");
        Guide guide3 = createdGuide("hello3");
        Guide guide4 = createdGuide("hello4");
        Guide guide5 = createdGuide("hello5");
        Guide guide6 = createdGuide("hello6");
        Guide guide7 = createdGuide("hello7");
        Guide guide8 = createdGuide("hello8");
        Guide guide9 = createdGuide("hello9");
        Guide guide10 = createdGuide("hello10");
        Guide guide11 = createdGuide("hello11");
        guideRepository.saveAll(List.of(guide1, guide2, guide3,
                guide4, guide5, guide6, guide7, guide8,
                guide9, guide10, guide11));

        //when
        GuidePageRes guidePageRes = guideRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(guidePageRes.getPageNumber()).isEqualTo(0);
        assertThat(guidePageRes.getPageSize()).isEqualTo(10);
        assertThat(guidePageRes.getTotalPage()).isEqualTo(2);
        assertThat(guidePageRes.getTotalCount()).isEqualTo(11);
    }

    @DisplayName("조회 시, 데이터들의 삭제상태값은 false인 데이터만 조회된다.")
    @Test
    void getListWithoutNotDeleted() throws Exception {
        //given
        Guide guide1 = createdGuide("hello1");
        Guide guide2 = createdGuide("hello2");
        Guide guide3 = createdGuide("hello3");
        guide3.deletedGuide();
        Guide guide4 = createdGuide("hello4");
        Guide guide5 = createdGuide("hello5");
        Guide guide6 = createdGuide("hello6");
        guide6.deletedGuide();
        Guide guide7 = createdGuide("hello7");
        Guide guide8 = createdGuide("hello8");
        Guide guide9 = createdGuide("hello9");
        guide9.deletedGuide();
        Guide guide10 = createdGuide("hello10");
        Guide guide11 = createdGuide("hello11");
        guideRepository.saveAll(List.of(guide1, guide2, guide3,
                guide4, guide5, guide6, guide7, guide8,
                guide9, guide10, guide11));

        //when
        GuidePageRes guidePageRes = guideRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(guidePageRes.getGuideResList()).hasSize(8)
                .extracting(GuideRes::getContent)
                .containsExactly("hello11", "hello10", "hello8",
                        "hello7", "hello5", "hello4", "hello2", "hello1");
    }

    private Guide createdGuide(String content) {
        Guide guide = Guide.of(GuideReq.builder().content(content).build());
        guide.connectMember(savedMember);
        return guide;
    }
}