package com.festival.domain.foodTruck.service;

import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckCreateResponse;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodTruckService {
    FoodTruckCreateResponse createFoodTruck(FoodTruckRequest foodTruckRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws Exception;

    FoodTruckResponse getFoodTruck(Long foodTruckId) throws Exception;

    Page<FoodTruckResponse> getFoodTruckList(int offset, Boolean state);
}
