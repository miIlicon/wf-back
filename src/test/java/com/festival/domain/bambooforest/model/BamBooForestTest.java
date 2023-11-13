package com.festival.domain.bambooforest.model;

import com.festival.domain.bambooforest.dto.BamBooForestReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BamBooForestTest {

    @DisplayName("대나무숲 게시물을 생성하면 삭제상태 초기값은 false이다.")
    @Test
    void createdBamBooForest() throws Exception {
        //given //when
        BamBooForest bamBooForest = BamBooForest.of(BamBooForestReq.builder()
                .content("hello")
                .build());

        //then
        assertThat(bamBooForest.isDeleted()).isEqualTo(false);
    }
}