package com.festival.domain.guide.dto;

import com.festival.domain.guide.model.Guide;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class GuideRes {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdDateTime;

    @Builder
    private GuideRes(Long id, String content, String username, LocalDateTime createdDateTime) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.createdDateTime = createdDateTime;
    }

    public static GuideRes of(Guide guide) {
        return GuideRes.builder()
                .id(guide.getId())
                .content(guide.getContent())
                .username(guide.getMember().getUsername())
                .createdDateTime(guide.getCreatedDate())
                .build();
    }
}
