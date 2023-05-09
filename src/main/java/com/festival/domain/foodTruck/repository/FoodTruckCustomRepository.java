package com.festival.domain.foodTruck.repository;

import com.festival.common.vo.SearchCond;
import com.festival.domain.foodTruck.data.entity.FoodTruck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodTruckCustomRepository {
    Page<FoodTruck> findFoodTrucksById(SearchCond cond, Pageable pageable);
}
