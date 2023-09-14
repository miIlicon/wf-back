package com.festival.domain.booth.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BulkInsertBooth {

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    private String status;
    private String content;
    private Long latitude;
    private Long longitude;
    private String subTitle;
    private String title;
    private String type;
    private Long viewCount;
    private Long imageId;
    private Long memberId;

    @Builder
    private BulkInsertBooth(String createdBy,
                           String lastModifiedBy,
                           String status, String content,
                            Long latitude, Long longitude,
                            String subTitle, String title, String type, Long imageId, Long memberId) {
        this.createdBy = createdBy;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = LocalDateTime.now();
        this.status = status;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.subTitle = subTitle;
        this.title = title;
        this.type = type;
        this.viewCount = 0L;
        this.imageId = imageId;
        this.memberId = memberId;
    }

}