package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import org.springframework.data.domain.Pageable;
import com.festival.domain.booth.controller.dto.BoothSearchRes;
import java.util.List;

public interface BoothRepositoryCustom {
    BoothPageRes getList(BoothListSearchCond boothListSearchCond);
    List<BoothSearchRes> searchBoothList(String keyword);


}
