package com.festival.domain.bambooforest.dto;

import com.festival.domain.bambooforest.model.BamBooForest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BamBooForestRes {

    private Long id;
    private String content;
    private LocalDateTime createdDate;

    @Builder
    public BamBooForestRes(Long id, String content, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static BamBooForestRes of(BamBooForest bamBooForest){
        return BamBooForestRes.builder()
                .id(bamBooForest.getId())
                .content(bamBooForest.getContent())
                .createdDate(bamBooForest.getCreatedDate())
                .build();
    }
}
