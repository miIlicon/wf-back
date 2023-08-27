package com.festival.domain.timetable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeTableDateReq {

    @NotNull(message = "시작 시간을 입력 해주세요.")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간을 입력 해주세요.")
    private LocalDateTime endTime;

    @NotNull(message = "상태를 선택 해주세요.")
    private String status;

    private int offset;
}
