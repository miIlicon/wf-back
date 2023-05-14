package com.festival.domain.info.festivalEvent.data.dto;

import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalEventRes {

    private Long festivalEventId;


    private String title;

    private String subTitle;

    private String content;

    private String mainFilePath;

    private List<String> subFilePaths;

    private int latitude;

    private int longitude;

    private Boolean festivalEventState;


    @Builder
    public FestivalEventRes(Long festivalEventId, String title, String subTitle, String content, String mainFilePath, List<String> subFilePaths, int latitude, int longitude, Boolean festivalEventState) {
        this.festivalEventId = festivalEventId;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
        this.latitude = latitude;
        this.longitude = longitude;
        this.festivalEventState = festivalEventState;
    }
    public static FestivalEventRes of(FestivalEvent festivalEvent, String filePath){

        List<String> list = new ArrayList<>();
        for (String subFilePath: festivalEvent.getFestivalEventImage().getSubFileNames()) {
            list.add(filePath + subFilePath);
        }

        return FestivalEventRes.builder()
                .festivalEventId(festivalEvent.getId())
                .title(festivalEvent.getTitle())
                .content(festivalEvent.getContent())
                .subTitle(festivalEvent.getSubTitle())
                .mainFilePath(festivalEvent.getFestivalEventImage().getMainFileName())
                .subFilePaths(list)
                .latitude(festivalEvent.getLatitude())
                .longitude(festivalEvent.getLongitude())
                .festivalEventState(festivalEvent.getFestivalEventState())
                .build();
    }
}
