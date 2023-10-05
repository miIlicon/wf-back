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
    private String username;

    @Builder
    private GuideRes(Long id, String content, String username) {
        this.id = id;
        this.content = content;
        this.username = username;
    }

    public static GuideRes of(Guide guide) {
        return GuideRes.builder()
                .id(guide.getId())
                .content(guide.getContent())
                .username(guide.getMember().getUsername())
                .build();
    }
}
