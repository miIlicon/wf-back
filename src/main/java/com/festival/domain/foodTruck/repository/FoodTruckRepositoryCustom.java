package com.festival.domain.foodTruck.repository;

import com.festival.common.vo.SearchCond;
import com.festival.domain.foodTruck.data.entity.FoodTruck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodTruckRepositoryCustom {
    Page<FoodTruck> findByIdTrucks(SearchCond cond, Pageable pageable);
}
