package com.festival.domain.bambooforest.repository;

import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.service.vo.BamBooForestSearchCond;

public interface BamBooForestRepositoryCustom {
    BamBooForestPageRes getList(BamBooForestSearchCond bamBooForestSearchCond);
}
