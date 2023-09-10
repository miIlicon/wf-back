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
    private String type;
    private String mainFilePath;
    private List<String> subFilePaths;

    @Builder
    private GuideRes(Long id, String title, String content, String type, String mainFilePath, List<String> subFilePaths) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
    }

    public static GuideRes of(Guide guide) {
        return GuideRes.builder()
                .id(guide.getId())
                .title(guide.getTitle())
                .content(guide.getContent())
                .type(guide.getType().getValue())
                .mainFilePath(guide.getImage().getMainFilePath())
                .subFilePaths(guide.getImage().getSubFilePaths())
                .build();
    }
}
