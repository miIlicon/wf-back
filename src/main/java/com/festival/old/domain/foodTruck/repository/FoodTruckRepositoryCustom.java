package com.festival.old.domain.foodTruck.repository;

import com.festival.old.common.vo.SearchCond;
import com.festival.old.domain.foodTruck.data.entity.FoodTruck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodTruckRepositoryCustom {
    Page<FoodTruck> findByIdTrucks(SearchCond cond, Pageable pageable);
}
