package com.festival.domain.guide.model;

import com.festival.domain.guide.dto.GuideReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GuideTest {

    @DisplayName("")
    @Test
    void whenCreatedGuideDeletedIsFalse() throws Exception {
        //given //when
        Guide guide = Guide.of(GuideReq.builder()
                .content("test1")
                .build());

        //then
        assertThat(guide.isDeleted()).isFalse();
    }
}