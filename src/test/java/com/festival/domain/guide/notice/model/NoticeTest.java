package com.festival.domain.guide.notice.model;

import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.guide.notice.model.Notice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoticeTest {

    @DisplayName("")
    @Test
    void whenCreatedGuideDeletedIsFalse() throws Exception {
        //given //when
        Notice notice = Notice.of(NoticeReq.builder()
                .content("test1")
                .build());

        //then
        assertThat(notice.isDeleted()).isFalse();
    }

    @DisplayName("안내사항 게시물의 조회수가 입력된 만큼 증가한다.")
    @Test
    void increaseViewCount() throws Exception {
        //given
        Notice notice = Notice.of(NoticeReq.builder()
                .content("test1")
                .build());

        //when
        notice.increaseViewCount(3L);

        //then
        assertThat(notice.getViewCount()).isEqualTo(3L);
    }
}