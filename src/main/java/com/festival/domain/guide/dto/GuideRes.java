package com.festival.domain.guide.dto;

import com.festival.domain.guide.model.Guide;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuideRes {

    private Long id;
    private String content;

    @Builder
    private GuideRes(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static GuideRes of(Guide guide) {
        return GuideRes.builder()
                .id(guide.getId())
                .content(guide.getContent())
                .build();
    }
}
