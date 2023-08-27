package com.festival.old.domain.info.festivalEvent.data.dto;

import com.festival.old.domain.info.festivalEvent.data.entity.FestivalEvent;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalEventRes {

    private String title;

    private String subTitle;

    private String content;

    private String mainFilePath;

    private List<String> subFilePaths;

    private float latitude;

    private float longitude;

    private Boolean state;


    @Builder
    public FestivalEventRes(String title, String subTitle, String content, String mainFilePath, List<String> subFilePaths, float latitude, float longitude, Boolean state) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }
    public static FestivalEventRes of(FestivalEvent festivalEvent, String filePath){

        List<String> list = new ArrayList<>();
        for (String subFilePath: festivalEvent.getFestivalEventImage().getSubFileNames()) {
            list.add(filePath + subFilePath);
        }

        return FestivalEventRes.builder()
                .title(festivalEvent.getTitle())
                .content(festivalEvent.getContent())
                .subTitle(festivalEvent.getSubTitle())
                .mainFilePath(festivalEvent.getFestivalEventImage().getMainFileName())
                .subFilePaths(list)
                .latitude(festivalEvent.getLatitude())
                .longitude(festivalEvent.getLongitude())
                .state(festivalEvent.getFestivalEventState())
                .build();
    }
}
