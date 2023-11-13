package com.festival.domain.bambooforest.repository;

import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.dto.BamBooForestRes;
import com.festival.domain.bambooforest.model.BamBooForest;
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
class BamBooForestRepositoryTest {

    @Autowired
    private BamBooForestRepository bamBooForestRepository;

    @DisplayName("대나무숲 게시물을 생성할 때, 내용이 없으면 예외가 발생한다.")
    @Test
    void createdBamBooForestWithoutContent() {
        //given
        BamBooForestReq bamBooForestReq = BamBooForestReq.builder()
                .content(null) // null로 설정
                .build();

        //when & then
        assertThatThrownBy(() -> {
            BamBooForest bamBooForest = BamBooForest.of(bamBooForestReq);
            bamBooForestRepository.save(bamBooForest);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("생성된 게시물들은 10개씩 페이징되며 조회된다.")
    @Test
    void getList() throws Exception {
        //given
        BamBooForest bamBooForest1 = createBamBooForest("hello1");
        BamBooForest bamBooForest2 = createBamBooForest("hello2");
        BamBooForest bamBooForest3 = createBamBooForest("hello3");
        BamBooForest bamBooForest4 = createBamBooForest("hello4");
        BamBooForest bamBooForest5 = createBamBooForest("hello5");
        BamBooForest bamBooForest6 = createBamBooForest("hello6");
        BamBooForest bamBooForest7 = createBamBooForest("hello7");
        BamBooForest bamBooForest8 = createBamBooForest("hello8");
        BamBooForest bamBooForest9 = createBamBooForest("hello9");
        BamBooForest bamBooForest10 = createBamBooForest("hello10");
        BamBooForest bamBooForest11 = createBamBooForest("hello11");
        bamBooForestRepository.saveAll(List.of(bamBooForest1, bamBooForest2, bamBooForest3,
                bamBooForest4, bamBooForest5, bamBooForest6, bamBooForest7, bamBooForest8,
                bamBooForest9, bamBooForest10, bamBooForest11));

        //when
        BamBooForestPageRes booForestPageRes = bamBooForestRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(booForestPageRes.getForestResList()).hasSize(10)
                .extracting(BamBooForestRes::getContent)
                .containsExactly("hello11", "hello10", "hello9", "hello8",
                        "hello7", "hello6", "hello5", "hello4", "hello3", "hello2");
    }

    @DisplayName("데이터를 조회할 때, 페이지 설정이 포함되어서 조회된다.")
    @Test
    void getListWithPageSetting() throws Exception {
        //given
        BamBooForest bamBooForest1 = createBamBooForest("hello1");
        BamBooForest bamBooForest2 = createBamBooForest("hello2");
        BamBooForest bamBooForest3 = createBamBooForest("hello3");
        BamBooForest bamBooForest4 = createBamBooForest("hello4");
        BamBooForest bamBooForest5 = createBamBooForest("hello5");
        BamBooForest bamBooForest6 = createBamBooForest("hello6");
        BamBooForest bamBooForest7 = createBamBooForest("hello7");
        BamBooForest bamBooForest8 = createBamBooForest("hello8");
        BamBooForest bamBooForest9 = createBamBooForest("hello9");
        BamBooForest bamBooForest10 = createBamBooForest("hello10");
        BamBooForest bamBooForest11 = createBamBooForest("hello11");
        bamBooForestRepository.saveAll(List.of(bamBooForest1, bamBooForest2, bamBooForest3,
                bamBooForest4, bamBooForest5, bamBooForest6, bamBooForest7, bamBooForest8,
                bamBooForest9, bamBooForest10, bamBooForest11));

        //when
        BamBooForestPageRes booForestPageRes = bamBooForestRepository.getList(PageRequest.of(0, 10));


        //then
        assertThat(booForestPageRes.getPageNumber()).isEqualTo(0);
        assertThat(booForestPageRes.getPageSize()).isEqualTo(10);
        assertThat(booForestPageRes.getTotalPage()).isEqualTo(2);
        assertThat(booForestPageRes.getTotalCount()).isEqualTo(11);
    }

    @DisplayName("조회 시, 데이터들의 삭제상태값은 false인 데이터만 조회된다.")
    @Test
    void getListWithoutNotDeleted() throws Exception {
        //given
        BamBooForest bamBooForest1 = createBamBooForest("hello1");
        BamBooForest bamBooForest2 = createBamBooForest("hello2");
        BamBooForest bamBooForest3 = createBamBooForest("hello3");
        bamBooForest3.deletedBambooForest();
        BamBooForest bamBooForest4 = createBamBooForest("hello4");
        BamBooForest bamBooForest5 = createBamBooForest("hello5");
        BamBooForest bamBooForest6 = createBamBooForest("hello6");
        BamBooForest bamBooForest7 = createBamBooForest("hello7");
        bamBooForest7.deletedBambooForest();
        BamBooForest bamBooForest8 = createBamBooForest("hello8");
        bamBooForest8.deletedBambooForest();
        BamBooForest bamBooForest9 = createBamBooForest("hello9");
        BamBooForest bamBooForest10 = createBamBooForest("hello10");
        BamBooForest bamBooForest11 = createBamBooForest("hello11");
        bamBooForestRepository.saveAll(List.of(bamBooForest1, bamBooForest2, bamBooForest3,
                bamBooForest4, bamBooForest5, bamBooForest6, bamBooForest7, bamBooForest8,
                bamBooForest9, bamBooForest10, bamBooForest11));

        //when
        BamBooForestPageRes booForestPageRes = bamBooForestRepository.getList(PageRequest.of(0, 10));

        //then
        assertThat(booForestPageRes.getForestResList()).hasSize(8)
                .extracting(BamBooForestRes::getContent)
                .containsExactly("hello11", "hello10", "hello9", "hello6",
                        "hello5", "hello4", "hello2", "hello1");
    }

    private BamBooForest createBamBooForest(String content) {
        return BamBooForest.of(BamBooForestReq.builder()
                .content(content)
                .build());
    }
}