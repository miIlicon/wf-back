package com.festival.domain.guide.repository.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GuideSearchCond {

    private String status;
    private Pageable pageable;

    @Builder
    private GuideSearchCond(String status, Pageable pageable) {
        this.status = status;
        this.pageable = pageable;
    }
}
