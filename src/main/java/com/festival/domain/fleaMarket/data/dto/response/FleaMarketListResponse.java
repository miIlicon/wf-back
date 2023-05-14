package com.festival.domain.fleaMarket.data.dto.response;

import com.festival.domain.fleaMarket.data.entity.FleaMarket;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FleaMarketListResponse {

    private Long id;
    private String title;
    private String subTitle;
    private String mainFilePath;
    private Boolean fleaMarketState;

    @Builder
    @QueryProjection
    public FleaMarketListResponse(Long id, String title, String subTitle,
                              String mainFilePath, Boolean fleaMarketState) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.mainFilePath = mainFilePath;
        this.fleaMarketState = fleaMarketState;
    }

    public static FleaMarketListResponse of(final FleaMarket fleaMarket, String filePath) {
        return FleaMarketListResponse.builder()
                .id(fleaMarket.getId())
                .title(fleaMarket.getTitle())
                .subTitle(fleaMarket.getSubTitle())
                .mainFilePath(filePath + fleaMarket.getFleaMarketImage().getMainFileName())
                .fleaMarketState(fleaMarket.getFleaMarketState())
                .build();
    }
}
