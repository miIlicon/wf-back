package com.festival.domain.foodTruck.service;

import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodTruckService {
    FoodTruckResponse createFoodTruck(FoodTruckRequest foodTruckRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws Exception;
}
