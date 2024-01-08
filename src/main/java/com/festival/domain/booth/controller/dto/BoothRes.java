package com.festival.domain.booth.controller.dto;

import com.festival.domain.booth.model.Booth;
import com.festival.domain.image.model.Image;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoothRes {
    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private float latitude;
    private float longitude;
    private String operateStatus;
    private String type;
    private String mainFilePath;
    private List<String> subFilePaths;

    @Builder
    @QueryProjection
    public BoothRes(Long id, String title, String subTitle, String content, float latitude, float longitude, String operateStatus, String type, String mainFilePath, List<String> subFilePaths) {
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

    public static BoothRes of(Booth booth){
        return BoothRes.builder()
                .id(booth.getId())
                .title(booth.getTitle())
                .subTitle(booth.getSubTitle())
                .content(booth.getContent())
                .latitude(booth.getLatitude())
                .longitude(booth.getLongitude())
                .operateStatus(booth.getOperateStatus().getValue())
                .type(booth.getType().getValue())
                .mainFilePath(booth.getThumbnailImage().getFilePath())
                .subFilePaths(booth.getImages().stream().map(Image::getFilePath).toList()).build();
    }
}
