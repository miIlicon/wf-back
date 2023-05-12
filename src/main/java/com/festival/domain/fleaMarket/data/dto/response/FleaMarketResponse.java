package com.festival.domain.fleaMarket.data.dto.response;

import com.festival.domain.fleaMarket.data.entity.FleaMarket;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarketResponse {

    private String title;

    private String subTitle;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String mainFilePath;

    private List<String> subFilePath;

    private int latitude;

    private int longitude;

    private Boolean fleeMarketState;

    @Builder
    @QueryProjection
    public FleaMarketResponse(String title, String subTitle, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, String mainFilePath, List<String> subFilePath, int latitude, int longitude, Boolean fleeMarketState) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.mainFilePath = mainFilePath;
        this.subFilePath = subFilePath;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fleeMarketState = fleeMarketState;
    }

    public static FleaMarketResponse of(final FleaMarket fleaMarket, String filePath) {
        if (!fleaMarket.getFleaMarketImage().getSubFileNames().isEmpty()) {
            List<String> list = new ArrayList<>();
            for (String subFilePath: fleaMarket.getFleaMarketImage().getSubFileNames()) {
                list.add(filePath + subFilePath);
            }
            return FleaMarketResponse.builder()
                    .title(fleaMarket.getTitle())
                    .subTitle(fleaMarket.getSubTitle())
                    .content(fleaMarket.getContent())
                    .createdDate(fleaMarket.getCreatedDate())
                    .modifiedDate(fleaMarket.getModifiedDate())
                    .mainFilePath(filePath + fleaMarket.getFleaMarketImage().getMainFileName())
                    .subFilePath(list)
                    .latitude(fleaMarket.getLatitude())
                    .longitude(fleaMarket.getLongitude())
                    .fleeMarketState(fleaMarket.getFleaMarketState())
                    .build();
        } else {
            return FleaMarketResponse.builder()
                    .title(fleaMarket.getTitle())
                    .content(fleaMarket.getContent())
                    .createdDate(fleaMarket.getCreatedDate())
                    .modifiedDate(fleaMarket.getModifiedDate())
                    .mainFilePath(filePath + fleaMarket.getFleaMarketImage().getMainFileName())
                    .latitude(fleaMarket.getLatitude())
                    .longitude(fleaMarket.getLongitude())
                    .fleeMarketState(fleaMarket.getFleaMarketState())
                    .build();
        }
    }
}
