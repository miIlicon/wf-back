package com.festival.domain.bambooforest.dto;

import com.festival.domain.bambooforest.model.BamBooForest;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BamBooForestRes {

    private Long id;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public BamBooForestRes(Long id, String content, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static BamBooForestRes of(BamBooForest bamBooForest){
        return BamBooForestRes.builder()
                .id(bamBooForest.getId())
                .content(bamBooForest.getContent())
                .createdAt(bamBooForest.getCreatedDate())
                .build();
    }
}
