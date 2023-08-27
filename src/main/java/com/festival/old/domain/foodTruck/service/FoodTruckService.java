package com.festival.old.domain.foodTruck.service;

import com.festival.old.common.base.CommonIdResponse;
import com.festival.old.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.old.domain.foodTruck.data.dto.response.FoodTruckListResponse;
import com.festival.old.domain.foodTruck.data.dto.response.FoodTruckResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FoodTruckService {
    CommonIdResponse createFoodTruck(FoodTruckRequest foodTruckRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws Exception;

    FoodTruckResponse getFoodTruck(Long foodTruckId) throws Exception;

    Page<FoodTruckListResponse> getFoodTruckList(int offset, boolean state);

    CommonIdResponse updateFoodTruck(Long foodTruckId, FoodTruckRequest foodTruckRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws IOException;

    CommonIdResponse deleteFoodTruck(Long foodTruckId);
}
