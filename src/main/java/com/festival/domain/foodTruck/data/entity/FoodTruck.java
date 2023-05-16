package com.festival.domain.foodTruck.data.entity;

import com.festival.common.base.BaseTimeEntity;
import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.domain.info.festivalPub.data.entity.file.PubImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodTruck extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "food_truck_image_id")
    private FoodTruckImage foodTruckImage;

    @Column(name = "latitude", nullable = false) // 위도
    private float latitude;

    @Column(name = "longitude", nullable = false) // 경도
    private float longitude;

    @Column(name = "pub_state", nullable = false)
    private Boolean foodTruckState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    public FoodTruck(String title, String subTitle, String content, float latitude, float longitude, Boolean foodTruckState) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foodTruckState = foodTruckState;
    }

    public FoodTruck(FoodTruckRequest foodTruckRequest) {
        this.title = foodTruckRequest.getTitle();
        this.subTitle = foodTruckRequest.getSubTitle();
        this.content = foodTruckRequest.getContent();
        this.latitude = foodTruckRequest.getLatitude();
        this.longitude = foodTruckRequest.getLongitude();
        this.foodTruckState = foodTruckRequest.getState();
    }

    public void connectAdmin(Admin admin) {
        this.admin = admin;
    }

    public void connectFoodTruckImage(FoodTruckImage foodTruckImage) {
        this.foodTruckImage = foodTruckImage;
    }


    public void modify(FoodTruckRequest foodTruckRequest) {
        this.title = foodTruckRequest.getTitle();
        this.subTitle = foodTruckRequest.getSubTitle();
        this.content = foodTruckRequest.getContent();
        this.latitude = foodTruckRequest.getLatitude();
        this.longitude = foodTruckRequest.getLongitude();
        this.foodTruckState = foodTruckRequest.getState();
    }
}
