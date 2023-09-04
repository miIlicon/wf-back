package com.festival.domain.booth.service.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class BoothListSearchCond {
    private String status;
    private String type;
    private Pageable pageable;


    @Builder
    public BoothListSearchCond(String status, String type, Pageable pageable) {
        this.status = status;
        this.type = type;
        this.pageable = pageable;
    }

}
