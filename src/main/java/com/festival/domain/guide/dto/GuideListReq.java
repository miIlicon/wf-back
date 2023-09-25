package com.festival.domain.guide.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuideListReq {

    @NotNull(message = "상태값을 입력해주세요.")
    String status;

    int page;

    int size;

    @Builder
    private GuideListReq(String status, int page, int size) {
        this.status = status;
        this.page = page;
        this.size = size;
    }
}
