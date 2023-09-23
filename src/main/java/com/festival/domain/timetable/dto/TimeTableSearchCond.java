package com.festival.domain.timetable.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimeTableSearchCond {
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    @Builder
    private TimeTableSearchCond(LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
