package com.festival.domain.booth.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class BoothReq {


    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "부제목을 입력해주세요")
    private String subTitle;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @NotNull(message = "위도를 입력 해주세요.")
    private float latitude;

    @NotNull(message = "경도를 입력 해주세요.")
    private float longitude;

    @NotNull(message = "상태를 선택 해주세요.")
    private String status;

    @NotNull(message = "타입을 선택 해주세요.")
    private String type;

    @NotNull(message = "썸네일 이미지를 선택해주세요")
    private MultipartFile mainFile;

    @NotNull(message = "서브 이미지를 선택해주세요")
    private List<MultipartFile> subFiles;
}
