package com.festival.domain.program.dto;

import lombok.Builder;

@Builder
public class ProgramRes {
    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private Float latitude;
    private Float longitude;
    private String status;
    private String type;
}
