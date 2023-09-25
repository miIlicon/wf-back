package com.festival.domain.program.service.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class ProgramSearchCond {

    private String status;
    private String type;
    private Pageable pageable;

    @Builder
    public ProgramSearchCond(String status, String type, Pageable pageable) {
        this.status = status;
        this.type = type;
        this.pageable = pageable;
    }

}
