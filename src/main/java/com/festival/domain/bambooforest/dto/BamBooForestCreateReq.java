package com.festival.domain.bambooforest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BamBooForestCreateReq {

    private String content;
    private String contact;
    private String status;

    @Builder
    public BamBooForestCreateReq(String content, String contact, String status) {
        this.content = content;
        this.contact = contact;
        this.status = status;
    }
}
