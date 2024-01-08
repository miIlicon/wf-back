package com.festival.domain.program.dto;

import com.festival.domain.image.model.Image;
import com.festival.domain.program.model.Program;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProgramRes {
    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private Float latitude;
    private Float longitude;
    private String operateStatus;
    private String type;
    private String mainFilePath;
    private List<String> subFilePaths;

    @Builder
    private ProgramRes(Long id, String title, String subTitle, String content, Float latitude, Float longitude, String operateStatus, String type, String mainFilePath, List<String> subFilePaths) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operateStatus = operateStatus;
        this.type = type;
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
    }

    public static ProgramRes of(Program program) {
        return ProgramRes.builder()
                .id(program.getId())
                .title(program.getTitle())
                .subTitle(program.getSubTitle())
                .content(program.getContent())
                .type(program.getType().toString())
                .operateStatus(program.getOperateStatus().toString())
                .longitude(program.getLongitude())
                .latitude(program.getLatitude())
                .mainFilePath(program.getThumbnailImage().getFilePath())
                .subFilePaths(program.getImages().stream().map(Image::getFilePath).toList())
                .build();
    }
}
