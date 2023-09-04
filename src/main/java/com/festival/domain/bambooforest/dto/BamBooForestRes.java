package com.festival.domain.bambooforest.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BamBooForestRes {
    private Long id;
    private String content;

    @QueryProjection
    public BamBooForestRes(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
