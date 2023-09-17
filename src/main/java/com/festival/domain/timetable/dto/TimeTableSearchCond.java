package com.festival.domain.timetable.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeTableSearchCond {
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public TimeTableSearchCond(LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
