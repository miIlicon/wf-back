package com.festival.old.domain.foodTruck.data.dto.response;

import com.festival.old.domain.foodTruck.data.entity.FoodTruck;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodTruckListResponse {

    private Long id;
    private String title;
    private String subTitle;
    private String mainFilePath;
    private Boolean state;

    @Builder
    @QueryProjection
    public FoodTruckListResponse(Long id, String title, String subTitle,
                                 String mainFilePath, Boolean state) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.mainFilePath = mainFilePath;
        this.state = state;
    }

    public static FoodTruckListResponse of(final FoodTruck foodTruck, String filePath) {
        return FoodTruckListResponse.builder()
                .id(foodTruck.getId())
                .title(foodTruck.getTitle())
                .subTitle(foodTruck.getSubTitle())
                .mainFilePath(filePath + foodTruck.getFoodTruckImage().getMainFileName())
                .state(foodTruck.getFoodTruckState())
                .build();
    }
}