package com.festival.domain.program.service.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class ProgramSearchCond {

    private String type;
    private Pageable pageable;

    @Builder
    public ProgramSearchCond(String type, Pageable pageable) {
        this.type = type;
        this.pageable = pageable;
    }

}
