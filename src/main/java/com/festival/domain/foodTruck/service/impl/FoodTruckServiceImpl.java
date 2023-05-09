package com.festival.domain.foodTruck.service.impl;

import com.festival.common.vo.SearchCond;
import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.foodTruck.data.dto.request.FoodTruckRequest;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckCreateResponse;
import com.festival.domain.foodTruck.data.dto.response.FoodTruckResponse;
import com.festival.domain.foodTruck.data.entity.FoodTruck;
import com.festival.domain.foodTruck.data.entity.FoodTruckImage;
import com.festival.domain.foodTruck.repository.FoodTruckImageRepository;
import com.festival.domain.foodTruck.repository.FoodTruckRepository;
import com.festival.domain.foodTruck.service.FoodTruckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FoodTruckServiceImpl implements FoodTruckService {
    private final AdminRepository adminRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final FoodTruckImageRepository foodTruckImageRepository;

    @Override
    public FoodTruckCreateResponse createFoodTruck(FoodTruckRequest foodTruckRequest, MultipartFile mainFile, List<MultipartFile> subFiles) throws Exception {
        log.debug("Start : FoodTruckServiceImpl : createFoodTruck");
        if (foodTruckRequest == null)
            throw new Exception("FoodTruckRequest Is Null");
        if (mainFile.getName().isBlank())
            throw new Exception("Main Image File Is Null");
        FoodTruckCreateResponse foodTruckResponse = null;
        try {
            //TODO: JWT_USER_PARSER_FOR_ADMIN
//            Admin admin = adminRepository.findById(jwtUserParser.getUserId()).orElseThrow(
//                    () -> new IllegalArgumentException("해당되는 유저가 존재하지 않습니다.")
//            );
            //임시 어드민
            Admin admin = new Admin();

            FoodTruckImage foodTruckImage = new FoodTruckImage(mainFile, subFiles);
            foodTruckImageRepository.save(foodTruckImage);

            FoodTruck foodTruck = FoodTruck.of(foodTruckRequest, foodTruckImage, admin);
            foodTruckRepository.save(foodTruck);

            foodTruckResponse = new FoodTruckCreateResponse(foodTruck.getId());
        } catch (RuntimeException re) {
            log.error("### FoodTruckServiceImpl createFoodTruck: RuntimeException occur ###");
            throw new RuntimeException("CreateFoodTruck Exception");
        }
        return foodTruckResponse;
    }

    @Override
    public FoodTruckResponse getFoodTruck(Long foodTruckId) throws Exception {
        log.debug("Start : FoodTruckServiceImpl : getFoodTruck");
        //TODO: JWT_USER_PARSER_FOR_ADMIN
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId).orElseThrow(() -> new IllegalArgumentException("해당 푸드트럭 게시글이 존재하지 않습니다."));
        return FoodTruckResponse.of(foodTruck);
    }

    @Override
    public Page<FoodTruckResponse> getFoodTruckList(int offset, Boolean state) {
        //TODO: JWT_USER_PARSER_FOR_ADMIN(어드민 임시 데이터 1L)
        Pageable pageable = PageRequest.of(offset, 6);
        SearchCond cond = new SearchCond(1L, state);

        Page<FoodTruck> foodTruckList = foodTruckRepository.findFoodTrucksById(cond, pageable);
        return foodTruckList.map(foodTruck -> FoodTruckResponse.of(foodTruck));
    }


}
