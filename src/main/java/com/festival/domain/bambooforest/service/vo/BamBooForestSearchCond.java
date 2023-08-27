package com.festival.domain.bambooforest.service.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class BamBooForestSearchCond {

    private String status;
    private Pageable pageable;

    @Builder
    private BamBooForestSearchCond(String status, Pageable pageable) {
        this.status = status;
        this.pageable = pageable;
    }
}
