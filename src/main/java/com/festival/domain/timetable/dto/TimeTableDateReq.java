package com.festival.domain.timetable.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TimeTableDateReq {

    @NotNull(message = "시작 시간을 입력 해주세요.")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간을 입력 해주세요.")
    private LocalDateTime endTime;

    @Builder
    private TimeTableDateReq(LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
