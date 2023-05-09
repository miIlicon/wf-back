package com.festival.domain.foodTruck.data.dto.response;

import com.festival.domain.foodTruck.data.entity.FoodTruck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodTruckResponse {
    private Long foodTruckId;

    private String title;

    private String subTitle;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String filePath;

    private List<String> subFilePath;

    private int latitude;

    private int longitude;

    private Boolean foodTruckState;

    public static FoodTruckResponse of(final FoodTruck foodTruck) {
        return FoodTruckResponse.builder()
                .foodTruckId(foodTruck.getId())
                .title(foodTruck.getTitle())
                .subTitle(foodTruck.getSubTitle())
                .content(foodTruck.getContent())
                .createdDate(foodTruck.getCreatedDate())
                .modifiedDate(foodTruck.getModifiedDate())
                .filePath(foodTruck.getFoodTruckImage().getMainFilePath())
                .subFilePath(foodTruck.getFoodTruckImage().getSubFilePaths())
                .latitude(foodTruck.getLatitude())
                .longitude(foodTruck.getLongitude())
                .foodTruckState(foodTruck.getFoodTruckState()).build();
    }
}
