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
    private String status;
    private String mainFilePath;

    @QueryProjection
    public ProgramSearchRes(Long id, String title, String subTitle, String status, String mainFilePath) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.status = status;
        this.mainFilePath = mainFilePath;
    }

}
