package com.festival.domain.booth.controller.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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


}
