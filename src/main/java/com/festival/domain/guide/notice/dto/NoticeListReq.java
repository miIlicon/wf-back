package com.festival.domain.guide.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeListReq {

    int page;
    int size;

    @Builder
    private NoticeListReq(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
