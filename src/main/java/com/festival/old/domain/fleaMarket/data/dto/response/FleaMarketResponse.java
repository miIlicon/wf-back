package com.festival.old.domain.fleaMarket.data.dto.response;

import com.festival.old.domain.fleaMarket.data.entity.FleaMarket;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarketResponse {

    private String title;
    private String subTitle;

    private String content;

    private String mainFilePath;
    private List<String> subFilePaths;

    private float latitude;
    private float longitude;

    private Boolean state;

    @Builder
    @QueryProjection
    public FleaMarketResponse(String title, String subTitle, String content,
                              String mainFilePath, List<String> subFilePaths, float latitude, float longitude, Boolean state) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.mainFilePath = mainFilePath;
        this.subFilePaths = subFilePaths;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }

    public static FleaMarketResponse of(final FleaMarket fleaMarket, String filePath) {
            List<String> list = new ArrayList<>();
            for (String subFilePath: fleaMarket.getFleaMarketImage().getSubFileNames()) {
                list.add(filePath + subFilePath);
            }
            return FleaMarketResponse.builder()
                    .title(fleaMarket.getTitle())
                    .subTitle(fleaMarket.getSubTitle())
                    .content(fleaMarket.getContent())
                    .mainFilePath(filePath + fleaMarket.getFleaMarketImage().getMainFileName())
                    .subFilePaths(list)
                    .latitude(fleaMarket.getLatitude())
                    .longitude(fleaMarket.getLongitude())
                    .state(fleaMarket.getFleaMarketState())
                    .build();
    }
}
