package com.festival.domain.foodTruck.controller;

import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckCreateResponse;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckResponse;
import com.festival.domain.foodTruck.service.FoodTruckService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/food-truck")
public class FoodTruckController {

    @Autowired
    private FoodTruckService foodTruckService;

    /*
     * FoodTruck 생성
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public FoodTruckCreateResponse createFoodTruck(@RequestPart("dto") @Valid FoodTruckRequest foodTruckRequest,
                                                   @RequestPart("main-file") @NotEmpty MultipartFile mainImageFile, @RequestPart("sub-file") List<MultipartFile> subImageFileList) throws Exception {
        log.debug("Start : FoodTruckController : createFoodTruck");
        //TODO: 입력값 검증
        FoodTruckCreateResponse foodTruckResponse = foodTruckService.createFoodTruck(foodTruckRequest, mainImageFile, subImageFileList);
        if (foodTruckResponse == null) {
            throw new Exception("FoodTruckResponse is Null");
        }
        return foodTruckResponse;
    }

    /*
     * FoodTruck 상세 조회
     */
    @GetMapping("/{id}")
    public FoodTruckResponse getFoodTruck(@PathVariable("id") Long foodTruckId) throws Exception {
        log.debug("Start : FoodTruckController : getFoodTruck");
        //TODO: 입력값 검증
        FoodTruckResponse foodTruckResponse = foodTruckService.getFoodTruck(foodTruckId);
        return foodTruckResponse;
    }

    /*
     * FoodTruck 목록 조회
     */
    @GetMapping("/list")
    public Page<FoodTruckResponse> getFoodTruckList(@RequestParam("page") int offset) {
        log.debug("Start : FoodTruckController : getFoodTruckList");
        //TODO: 입력값 검증
        Page<FoodTruckResponse> foodTruckResponseList = foodTruckService.getFoodTruckList(offset);
        return foodTruckResponseList;
    }

    /*
     * FoodTruck 수정
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public FoodTruckResponse updateFoodTruck(@PathVariable("id") Long foodTruckId, @RequestPart("dto") @Valid FoodTruckRequest foodTruckRequest,
                                             @RequestPart("main-file") @NotEmpty MultipartFile mainImageFile, @RequestPart("sub-file") List<MultipartFile> subImageFileList) throws IOException {
        log.debug("Start : FoodTruckController : updateFoodTruck");
        //TODO: 입력값 검증
        FoodTruckResponse foodTruckResponse = foodTruckService.updateFoodTruck(foodTruckId, foodTruckRequest, mainImageFile, subImageFileList);
        return foodTruckResponse;
    }

    /*
     * FoodTruck 삭제
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteFoodTruck(@PathVariable("id") Long foodTruckId) {
        log.debug("Start : FoodTruckController : deleteFoodTruck");
        //TODO: 입력값 검증
        foodTruckService.deleteFoodTruck(foodTruckId);
        return;
    }
}
