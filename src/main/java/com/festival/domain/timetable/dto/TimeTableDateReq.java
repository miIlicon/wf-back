package com.festival.domain.timetable.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeTableDateReq {

    @NotNull(message = "시작 시간을 입력 해주세요.")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간을 입력 해주세요.")
    private LocalDateTime endTime;

    @NotNull(message = "상태를 선택 해주세요.")
    private String status;

}
