package com.festival.domain.program.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
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

    @NotNull(message = "상태를 입력해주세요.")
    private String status;

    @NotNull(message = "썸네일 이미지를 선택해주세요")
    private MultipartFile mainFile;

    @NotNull(message = "서브 이미지를 선택해주세요")
    private List<MultipartFile> subFiles;

    @Builder
    private ProgramReq(String title, String subTitle, String content, Float latitude, Float longitude, String type, String status, MultipartFile mainFile, List<MultipartFile> subFiles) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.status = status;
        this.mainFile = mainFile;
        this.subFiles = subFiles;
    }
}
