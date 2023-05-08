package com.festival.domain.foodTruck.repository;

import com.festival.domain.foodTruck.data.entity.FoodTruckImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodTruckImageRepository extends JpaRepository<FoodTruckImage, Long> {
}
