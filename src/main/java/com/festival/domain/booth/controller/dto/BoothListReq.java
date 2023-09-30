package com.festival.domain.booth.controller.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoothListReq {

    @NotNull(message = "타입을 선택 해주세요.")
    @Parameter(name = "type" , description = "PUB, FOOD_TRUCK, FLEA_MARKET")
    private String type;

    private int size;
    private int page;

    @Builder
    private BoothListReq(String type, int size, int page) {
        this.type = type;
        this.page = page;
        this.size = size;
    }
}
