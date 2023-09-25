package com.festival.domain.bambooforest.repository;

import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import org.springframework.data.domain.Pageable;

public interface BamBooForestRepositoryCustom {
    BamBooForestPageRes getList(Pageable pageable);
}
