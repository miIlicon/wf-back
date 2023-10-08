package com.festival.domain.booth.controller.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoothSearchRes {

    private Long id;
    private String title;
    private String subTitle;
    private String operateStatus;
    private String mainFilePath;

    @QueryProjection
    public BoothSearchRes(Long id, String title, String subTitle, String operateStatus, String mainFilePath) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.operateStatus = operateStatus;
        this.mainFilePath = mainFilePath;
    }

}
