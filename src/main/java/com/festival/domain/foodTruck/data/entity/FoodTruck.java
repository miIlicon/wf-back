package com.festival.domain.foodTruck.data.entity;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class FoodTruck extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "food_truck_image_id", referencedColumnName = "id")
    private FoodTruckImage foodTruckImage;

    @Column(name = "latitude", nullable = false) // 위도
    private int latitude;

    @Column(name = "longitude", nullable = false) // 경도
    private int longitude;

    @Column(name = "foodTruck_state", nullable = false)
    private Boolean foodTruckState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festivalEvent_admin_id", referencedColumnName = "id", nullable = false)
    private Admin admin;

    public static FoodTruck of(FoodTruckRequest foodTruckRequest, FoodTruckImage foodTruckImage, Admin admin) {
        return FoodTruck.builder()
                .title(foodTruckRequest.getTitle())
                .subTitle(foodTruckRequest.getSubTitle())
                .content(foodTruckRequest.getContent())
                .latitude(foodTruckRequest.getLatitude())
                .longitude(foodTruckRequest.getLongitude())
                .foodTruckState(foodTruckRequest.getFoodTruckState())
                .foodTruckImage(foodTruckImage)
                .admin(admin)
                .build();
    }

}
