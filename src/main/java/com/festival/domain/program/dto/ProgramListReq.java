package com.festival.domain.program.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class ProgramListReq {
    String status;
    String type;

    @Builder
    public ProgramListReq(String status, String type) {
        this.status = status;
        this.type = type;
    }
}
