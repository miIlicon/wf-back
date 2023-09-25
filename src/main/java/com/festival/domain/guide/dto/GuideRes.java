package com.festival.domain.guide.dto;

import com.festival.domain.guide.model.Guide;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GuideRes {

    private Long id;
    private String title;
    private String content;

    @Builder
    private GuideRes(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static GuideRes of(Guide guide) {
        return GuideRes.builder()
                .id(guide.getId())
                .title(guide.getTitle())
                .content(guide.getContent())
                .build();
    }
}
