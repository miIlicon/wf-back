package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoothRepositoryCustom {
    List<BoothRes> getList(BoothListSearchCond boothListSearchCond, Pageable pageable);
}
