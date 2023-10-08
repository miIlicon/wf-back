package com.festival.domain.guide.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GuideReq {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    private GuideReq(String content) {
        this.content = content;
    }

}
