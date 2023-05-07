package com.festival.domain.foodTruck.controller;

import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckResponse;
import com.festival.domain.foodTruck.service.FoodTruckService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    @PostMapping("/new")
    public FoodTruckResponse createFoodTruck(@RequestPart("dto") @Valid FoodTruckRequest foodTruckRequest,
                                             @RequestPart("main-file") @NotEmpty MultipartFile multipartFile, @RequestPart("sub-file") List<MultipartFile> multipartFileList) throws IOException {
        log.debug("Start : FoodTruckController : createFoodTruck");
        //TODO: 입력값 검증
        //TODO: 생성로직
        FoodTruckResponse foodTruckResponse = new FoodTruckResponse();
        return foodTruckResponse;
    }

    /*
     * FoodTruck 상세 조회
     */
    @GetMapping("/{foodTruckId}")
    public FoodTruckResponse getFoodTruck(@PathVariable String foodTruckId) {
        log.debug("Start : FoodTruckController : getFoodTruck");
        //TODO: 입력값 검증
        //TODO: 조회로직
        FoodTruckResponse foodTruckResponse = new FoodTruckResponse();
        return foodTruckResponse;
    }

    /*
     * FoodTruck 목록 조회
     */
    @GetMapping("/list")
    public List<FoodTruckResponse> getFoodTruckList() {
        log.debug("Start : FoodTruckController : getFoodTruckList");
        //TODO: 입력값 검증
        //TODO: 목록조회로직
        FoodTruckResponse foodTruckResponse = new FoodTruckResponse();
        List<FoodTruckResponse> foodTruckResponseList = new ArrayList<>();
        foodTruckResponseList.add(foodTruckResponse);
        return foodTruckResponseList;
    }

    /*
     * FoodTruck 수정
     */
    @PutMapping("/{foodTruckId}")
    public FoodTruckResponse updateFoodTruck(@PathVariable String foodTruckId) {
        log.debug("Start : FoodTruckController : updateFoodTruck");
        //TODO: 입력값 검증
        //TODO: 수정로직
        FoodTruckResponse foodTruckResponse = new FoodTruckResponse();
        return foodTruckResponse;
    }

    /*
     * FoodTruck 삭제
     */
    @DeleteMapping("/{foodTruckId}")
    public FoodTruckResponse deleteFoodTruck(@PathVariable String foodTruckId) {
        log.debug("Start : FoodTruckController : deleteFoodTruck");
        //TODO: 입력값 검증
        //TODO: 삭제로직
        FoodTruckResponse foodTruckResponse = new FoodTruckResponse();
        return foodTruckResponse;
    }
}
