package com.festival.domain.program.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProgramSearchRes {

    private Long id;
    private String title;
    private String subTitle;
    private String operateStatus;
    private String mainFilePath;

    @QueryProjection
    public ProgramSearchRes(Long id, String title, String subTitle, String operateStatus, String mainFilePath) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.operateStatus = operateStatus;
        this.mainFilePath = mainFilePath;
    }
}
