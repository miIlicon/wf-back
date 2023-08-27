package com.festival.domain.program.service.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProgramSearchCond {
    String status;
    String type;

    @Builder
    public ProgramSearchCond(String status, String type) {
        this.status = status;
        this.type = type;
    }

}
