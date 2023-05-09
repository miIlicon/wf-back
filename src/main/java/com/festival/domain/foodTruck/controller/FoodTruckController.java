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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/{foodTruckId}")
    public FoodTruckResponse getFoodTruck(@PathVariable Long foodTruckId) throws Exception {
        log.debug("Start : FoodTruckController : getFoodTruck");
        //TODO: 입력값 검증
        FoodTruckResponse foodTruckResponse = foodTruckService.getFoodTruck(foodTruckId);
        return foodTruckResponse;
    }

    /*
     * FoodTruck 목록 조회
     */
    @GetMapping("/list")
    public Page<FoodTruckResponse> getFoodTruckList(@RequestParam("page") int offset, @RequestParam("state") Boolean state) {
        log.debug("Start : FoodTruckController : getFoodTruckList");
        //TODO: 입력값 검증
        Page<FoodTruckResponse> foodTruckResponseList = foodTruckService.getFoodTruckList(offset, state);
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
