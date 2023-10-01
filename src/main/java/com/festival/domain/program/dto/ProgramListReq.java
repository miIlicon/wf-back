package com.festival.domain.program.dto;


import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProgramListReq {

    @NotNull(message = "타입을 선택해주세요.")
    @Parameter(name = "type", description = "EVENT, GAME")
    String type;

    int page;

    int size;

    @Builder
    private ProgramListReq(String type, int page, int size) {
        this.type = type;
        this.page = page;
        this.size = size;
    }

}
