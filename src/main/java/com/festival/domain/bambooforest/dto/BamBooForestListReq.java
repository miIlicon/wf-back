package com.festival.domain.bambooforest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BamBooForestListReq {

    private int size;

    private int page;

}
