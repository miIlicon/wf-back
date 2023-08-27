package com.festival.domain.timetable.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TimeTableRes {

    private String title;

    @QueryProjection
    public TimeTableRes(String title) {
        this.title = title;
    }
}
