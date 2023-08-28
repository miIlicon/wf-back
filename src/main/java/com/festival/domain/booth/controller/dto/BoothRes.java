package com.festival.domain.booth.controller.dto;

import com.festival.domain.booth.model.Booth;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothRes {

    private Long id;

    private String title;

    private String subTitle;

    private String content;

    private float latitude;

    private float longitude;

    private String status;

    private String type;

    @Builder
    @QueryProjection
    public BoothRes(Long id, String title, String subTitle, String content, float latitude, float longitude, String status, String type) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.type = type;
    }




    public static BoothRes of(Booth booth){
        return BoothRes.builder()
                .title(booth.getTitle())
                .subTitle(booth.getSubTitle())
                .content(booth.getContent())
                .latitude(booth.getLatitude())
                .longitude(booth.getLongitude())
                .status(booth.getStatus().getValue())
                .type(booth.getType().getValue()).build();
    }
}
