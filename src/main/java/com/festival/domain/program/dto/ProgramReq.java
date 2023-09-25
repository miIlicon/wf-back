package com.festival.domain.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProgramReq {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "소제목을 입력해주세요.")
    private String subTitle;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "위치를 입력해주세요.")
    private Float latitude;

    @NotNull(message = "위치를 입력해주세요.")
    private Float longitude;

    @NotNull(message = "타입을 입력해주세요.")
    private String type;

    @NotNull(message = "운영 상태를 선택해주세요.")
    private String operateStatus;

    @NotNull(message = "썸네일 이미지를 선택해주세요.")
    private MultipartFile mainFile;

    @NotNull(message = "서브 이미지를 선택해주세요.")
    private List<MultipartFile> subFiles;

    @NotNull(message = "시작 시간을 선택해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "종료 시간을 선택해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    private ProgramReq(String title, String subTitle, String content, Float latitude, Float longitude, String type, String operateStatus,
                       MultipartFile mainFile, List<MultipartFile> subFiles,
                       LocalDate startDate, LocalDate endDate) {
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
