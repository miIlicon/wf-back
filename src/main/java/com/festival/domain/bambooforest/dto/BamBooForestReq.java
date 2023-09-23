package com.festival.domain.bambooforest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
public class BamBooForestReq {

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private String contact;

    @NotNull(message = "상태값을 입력해주세요")
    private String status;

    @Builder
    private BamBooForestReq(String content, String contact, String status) {
        this.content = content;
        this.contact = contact;
        this.status = status;
    }
}
