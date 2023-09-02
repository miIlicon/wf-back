package com.festival.domain.program.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgramListReq {

    @NotNull(message = "상태를 선택 해주세요.")
    String status;

    @NotNull(message = "타입을 선택 해주세요.")
    String type;

}
