package com.festival.domain.booth.service.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoothListSearchCond {
    private String status;
    private String type;


    @Builder
    public BoothListSearchCond(String status, String type) {
        this.status = status;
        this.type = type;
    }

}
