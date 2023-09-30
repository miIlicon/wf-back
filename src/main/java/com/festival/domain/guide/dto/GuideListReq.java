package com.festival.domain.guide.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GuideListReq {

    int page;

    int size;

    @Builder
    private GuideListReq(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
