package com.festival.domain.bambooforest.repository;

import com.festival.domain.bambooforest.dto.BamBooForestRes;
import com.festival.domain.bambooforest.service.vo.BamBooForestSearchCond;
import org.springframework.data.domain.Page;

public interface BamBooForestRepositoryCustom {
    Page<BamBooForestRes> getList(BamBooForestSearchCond bamBooForestSearchCond);
}
