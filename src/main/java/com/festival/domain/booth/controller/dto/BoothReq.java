package com.festival.domain.booth.controller.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoothReq {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "부제목을 입력해주세요")
    private String subTitle;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @NotNull(message = "위도를 입력해주세요.")
    private float latitude;

    @NotNull(message = "경도를 입력해주세요.")
    private float longitude;

    @NotNull(message = "타입을 선택해주세요.")
    @Parameter(name = "type" , description = "FOOD_TRUCK, PUB, FLEA_MARKET")
    private String type;

    @NotNull(message = "운영 상태를 선택해주세요.")
    @Parameter(name = "operateStatus", description = "OPERATE, TERMINATE, UPCOMING")
    private String operateStatus;

    @NotNull(message = "시작 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(name = "startDate" , description = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Parameter(name = "endDate" , description = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "썸네일 이미지를 선택해주세요")
    private MultipartFile mainFile;

    @NotNull(message = "서브 이미지를 선택해주세요")
    private List<MultipartFile> subFiles;

    @Builder
    private BoothReq(String title, String subTitle, String content, float latitude, float longitude, String type, String operateStatus, MultipartFile mainFile, List<MultipartFile> subFiles, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.operateStatus = operateStatus;
        this.mainFile = mainFile;
        this.subFiles = subFiles;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
