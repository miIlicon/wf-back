package com.festival.domain.guide.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NoticeReq {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    private NoticeReq(String content) {
        this.content = content;
    }

}
