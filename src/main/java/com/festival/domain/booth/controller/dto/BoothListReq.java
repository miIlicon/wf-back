package com.festival.domain.booth.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothListReq {

    @NotNull(message = "상태를 선택 해주세요.")
    private String status;

    @NotNull(message = "타입을 선택 해주세요.")
    private String type;
}
