package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.festival.domain.booth.controller.dto.BoothSearchRes;
import java.util.List;

public interface BoothRepositoryCustom {
    BoothPageRes getBoothList(BoothListSearchCond boothListSearchCond);
    List<BoothSearchRes> searchBoothList(String keyword);
}
