package com.festival.domain.foodTruck.repository;

import com.festival.domain.foodTruck.data.entity.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long>, FoodTruckRepositoryCustom {


}
