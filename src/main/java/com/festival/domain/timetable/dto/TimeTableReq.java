package com.festival.domain.timetable.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TimeTableReq {

    @NotNull(message = "시작 시간을 입력 해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Parameter(name = "startTime" , description = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "종료 시간을 입력 해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Parameter(name = "endTime" , description = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;

    @Builder
    private TimeTableReq(LocalDateTime startTime, LocalDateTime endTime, String title, String status) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }
}
