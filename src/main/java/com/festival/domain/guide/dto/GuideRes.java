package com.festival.domain.guide.dto;

import com.festival.domain.guide.model.Guide;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuideRes {

    private Long id;
    private String title;
    private String content;
    private String type;

    @Builder
    private GuideRes(Long id, String title, String content, String type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
    }

    @QueryProjection
    public GuideRes(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static GuideRes of(Guide guide) {
        return GuideRes.builder()
                .title(guide.getTitle())
                .content(guide.getContent())
                .type(guide.getGuideType().getValue())
                .build();
    }
}
