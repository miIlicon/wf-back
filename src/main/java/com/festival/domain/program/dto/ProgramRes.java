package com.festival.domain.program.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

public class ProgramRes {
    private String title;
    private String subTitle;
    private String content;
    private Float latitude;
    private Float longitude;
    private String status;
    private String type;

    @Builder
    @QueryProjection
    public ProgramRes(String title, String subTitle, String content, Float latitude, Float longitude, String status, String type) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.type = type;
    }
}
