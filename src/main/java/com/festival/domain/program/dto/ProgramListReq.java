package com.festival.domain.program.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
public class ProgramListReq {

    @NotNull(message = "상태를 선택 해주세요.")
    String status;

    @NotNull(message = "타입을 선택 해주세요.")
    String type;

    @Builder
    private ProgramListReq(String status, String type) {
        this.status = status;
        this.type = type;
    }
}
