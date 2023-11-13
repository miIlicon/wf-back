package com.festival.domain.timetable.model;

import com.festival.domain.timetable.dto.TimeTableReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TimeTableTest {

    @DisplayName("시간표 게시물을 생성하면 삭제상태 초기값은 false이다.")
    @Test
    void createdTimeTable() throws Exception {
        //given //when
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 13, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 11, 14, 0, 0, 0);
        TimeTable timeTable = TimeTable.of(TimeTableReq.builder()
                .title("hello")
                .startTime(startTime)
                .endTime(endTime)
                .build());

        //then
        assertThat(timeTable.isDeleted()).isEqualTo(false);
    }
}