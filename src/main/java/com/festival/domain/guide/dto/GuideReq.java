package com.festival.domain.guide.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuideReq {

    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력 해주세요.")
    private String content;

    @NotNull(message = "안내사항 타입을 입력 해주세요.")
    private String type;

    @NotNull(message = "상태값을 입력해주세요")
    private String status;
}
