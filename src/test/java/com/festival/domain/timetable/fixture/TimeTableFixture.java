package com.festival.domain.timetable.fixture;

import com.festival.domain.timetable.model.TimeTable;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeTableFixture {
    private static LocalDateTime now = LocalDateTime.now();

    public static final TimeTable TERMINATED_TIMETABLE = TimeTable.builder()
            .title("동아리 공연")
            .startTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute()))
            .endTime(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 2, now.getMinute()))
            .build();

}
