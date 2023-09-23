package com.festival.domain.bambooforest.dto;

import com.festival.domain.bambooforest.model.BamBooForest;
import lombok.*;

@Getter
@NoArgsConstructor
public class BamBooForestRes {

    private Long id;
    private String content;

    @Builder
    public BamBooForestRes(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static BamBooForestRes of(BamBooForest bamBooForest){
        return BamBooForestRes.builder()
                .id(bamBooForest.getId())
                .content(bamBooForest.getContent()).build();
    }
}
