package com.festival.domain.timetable.fixture;

import com.festival.domain.timetable.model.TimeTable;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeTableFixture {

    private static LocalDateTime registeredDate = LocalDateTime.of(2023, 9, 26, 10, 0);

    public static final TimeTable DELETED_TIMETABLE = TimeTable.builder()
            .title("동아리 공연")
            .startTime(LocalDateTime.of(registeredDate.getYear(), registeredDate.getMonth(), registeredDate.getDayOfMonth(), registeredDate.getHour(), registeredDate.getMinute()))
            .endTime(LocalDateTime.of(registeredDate.getYear(), registeredDate.getMonth(), registeredDate.getDayOfMonth(), registeredDate.getHour() + 2, registeredDate.getMinute()))
            .deleted(true)
            .build();

}
