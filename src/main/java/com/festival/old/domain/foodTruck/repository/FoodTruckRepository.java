package com.festival.old.domain.foodTruck.repository;

import com.festival.old.domain.foodTruck.data.entity.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long>, FoodTruckRepositoryCustom {


}
