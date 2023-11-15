package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.controller.dto.BoothSearchRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.image.model.Image;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.festival.common.base.OperateStatus.OPERATE;
import static com.festival.domain.booth.model.BoothType.PUB;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class BoothRepositoryTest {

    @Autowired
    private BoothRepository boothRepository;

    @DisplayName("시작일과 같으면서 운영중인 부스를 조회한다.")
    @Test
    void findByStartDateEqualsAndOperateStatusEquals() throws Exception {
        //given
        LocalDate startedDate = LocalDate.of(2023, 11, 14);
        LocalDate endedDate = LocalDate.of(2023, 11, 15);

        Booth booth1 = createdBooth(startedDate, endedDate, "title1");
        Booth booth2 = createdBooth(startedDate, endedDate, "title2");
        Booth booth3 = createdBooth(startedDate, endedDate, "title3");
        boothRepository.saveAllAndFlush(List.of(booth1, booth2, booth3));

        //when
        List<Booth> booths = boothRepository.findByStartDateEqualsAndOperateStatusEquals(startedDate, OPERATE);

        //then
        assertThat(booths).hasSize(3)
                .extracting("title")
                .containsExactlyInAnyOrder("title1", "title2", "title3");
    }

    @DisplayName("종료일이 같은 부스를 조회한다.")
    @Test
    void findByEndDateEquals() throws Exception {
        //given
        LocalDate startedDate = LocalDate.of(2023, 11, 14);
        LocalDate endedDate = LocalDate.of(2023, 11, 15);

        Booth booth1 = createdBooth(startedDate, endedDate, "title1");
        Booth booth2 = createdBooth(startedDate, endedDate, "title2");
        Booth booth3 = createdBooth(startedDate, endedDate, "title3");
        boothRepository.saveAllAndFlush(List.of(booth1, booth2, booth3));

        //when
        List<Booth> booths = boothRepository.findByEndDateEquals(endedDate);

        //then
        assertThat(booths).hasSize(3)
                .extracting("title")
                .containsExactlyInAnyOrder("title1", "title2", "title3");
    }

    @DisplayName("부스 리스트를 조회한다.")
    @Test
    void getBoothList() throws Exception {
        //given
        LocalDate startedDate = LocalDate.of(2023, 11, 14);
        LocalDate endedDate = LocalDate.of(2023, 11, 15);

        Booth booth1 = createdBooth(startedDate, endedDate, "title1");
        Booth booth2 = createdBooth(startedDate, endedDate, "title2");
        Booth booth3 = createdBooth(startedDate, endedDate, "title3");
        boothRepository.saveAllAndFlush(List.of(booth1, booth2, booth3));

        //when
        BoothListSearchCond boothListSearchCond = new BoothListSearchCond("OPERATE", "PUB", PageRequest.of(0, 6));
        BoothPageRes boothPageRes = boothRepository.getBoothList(boothListSearchCond);

        //then
        assertThat(boothPageRes.getBoothResList()).hasSize(3)
                .extracting("title")
                .containsExactlyInAnyOrder("title1", "title2", "title3");
        assertThat(boothPageRes.getPageNumber()).isEqualTo(0);
        assertThat(boothPageRes.getTotalCount()).isEqualTo(3);
        assertThat(boothPageRes.getTotalPage()).isEqualTo(1);
        assertThat(boothPageRes.getPageSize()).isEqualTo(6);
    }

    @DisplayName("키워드가 포함된 부스 리스트를 조회한다.")
    @Test
    void searchBoothList() throws Exception {
        //given
        LocalDate startedDate = LocalDate.of(2023, 11, 14);
        LocalDate endedDate = LocalDate.of(2023, 11, 15);

        Booth booth1 = createdBooth(startedDate, endedDate, "title1");
        Booth booth2 = createdBooth(startedDate, endedDate, "title2");
        Booth booth3 = createdBooth(startedDate, endedDate, "title3");
        boothRepository.saveAllAndFlush(List.of(booth1, booth2, booth3));

        //when
        String keyword = "title2";
        List<BoothSearchRes> boothSearchRes = boothRepository.searchBoothList(keyword);

        //then
        assertThat(boothSearchRes).hasSize(1)
                .extracting("title")
                .containsExactlyInAnyOrder("title2");
        assertThat(boothSearchRes.get(0)).isNotNull()
                .extracting("title", "subTitle", "operateStatus", "mainFilePath")
                .containsExactly("title2", "title2", "OPERATE", "mainFilePath");
    }

    private Booth createdBooth(LocalDate startedDate, LocalDate endedDate, String title) {
        Booth booth = Booth.builder()
                .title(title)
                .subTitle(title)
                .content("content")
                .latitude(1.0f)
                .longitude(1.0f)
                .operateStatus(OPERATE)
                .type(PUB)
                .startDate(startedDate)
                .endDate(endedDate)
                .deleted(false)
                .build();
        booth.setImage(Image.builder()
                .mainFilePath("mainFilePath")
                .subFilePaths(List.of("subFilePaths"))
                .build());
        return booth;
    }
}