package com.festival.domain.timetable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimeTableReq {

    @NotNull(message = "시작 시간을 입력 해주세요.")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간을 입력 해주세요.")
    private LocalDateTime endTime;

    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;

    @NotNull(message = "상태를 선택 해주세요.")
    private String status;

    @Builder
    private TimeTableReq(LocalDateTime startTime, LocalDateTime endTime, String title, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.status = status;
    }
}
