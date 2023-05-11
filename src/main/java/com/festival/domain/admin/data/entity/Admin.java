package com.festival.domain.admin.data.entity;

import com.festival.domain.foodTruck.data.entity.FoodTruck;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "admin")
    private List<Pub> pubs = new ArrayList<>();

    @OneToMany(mappedBy = "admin")
    private List<FoodTruck> foodTruckList = new ArrayList<>();
}
