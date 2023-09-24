package com.festival.domain.bambooforest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BamBooForestListReq {

    @NotNull(message = "상태값을 입력해주세요")
    private String status;

    private int size;

    private int page;

}
