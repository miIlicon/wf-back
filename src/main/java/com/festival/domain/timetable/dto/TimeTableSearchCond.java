package com.festival.domain.timetable.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeTableSearchCond {
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private int offset;

    public TimeTableSearchCond(LocalDateTime startTime, LocalDateTime endTime, String status, int offset) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.offset = offset;
    }
}
