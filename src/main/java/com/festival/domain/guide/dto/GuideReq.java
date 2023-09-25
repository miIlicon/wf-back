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

    @NotBlank(message = "제목을 입력 해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력 해주세요.")
    private String content;

    @NotNull(message = "안내사항 타입을 입력 해주세요.")
    private String type;

    @Builder
    private GuideReq(String title, String content, String type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

}
