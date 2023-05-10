package com.festival.domain.info.festivalEvent.data.dto;

import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalEventRes {

    private Long festivalEventId;


    private String title;

    private String subTitle;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String filePath;

    private List<String> subFilePath;

    private int latitude;

    private int longitude;

    private Boolean festivalEventState;

    @Builder
    public FestivalEventRes(Long festivalEventId, String title, String subTitle, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, String filePath, List<String> subFilePath, int latitude, int longitude, Boolean festivalEventState) {
        this.festivalEventId = festivalEventId;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.filePath = filePath;
        this.subFilePath = subFilePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.festivalEventState = festivalEventState;
    }
    public static FestivalEventRes of(FestivalEvent festivalEvent){
        return FestivalEventRes.builder()
                .festivalEventId(festivalEvent.getId())
                .title(festivalEvent.getTitle())
                .content(festivalEvent.getContent())
                .subTitle(festivalEvent.getSubTitle())
                .createdDate(festivalEvent.getCreatedDate())
                .modifiedDate(festivalEvent.getModifiedDate())
                .filePath(festivalEvent.getFestivalEventImage().getMainFilePath())
                .subFilePath(festivalEvent.getFestivalEventImage().getSubFilePaths())
                .latitude(festivalEvent.getLatitude())
                .longitude(festivalEvent.getLongitude())
                .festivalEventState(festivalEvent.getFestivalEventState())
                .build();
    }
}
