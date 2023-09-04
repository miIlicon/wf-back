package com.festival.domain.bambooforest.dto;

import com.festival.domain.bambooforest.model.BamBooForest;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BamBooForestRes {

    private Long id;
    private String content;

    public static BamBooForestRes of(BamBooForest bamBooForest){
        return BamBooForestRes.builder()
                .id(bamBooForest.getId())
                .content(bamBooForest.getContent()).build();
    }
}
