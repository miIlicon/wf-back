package com.festival.domain.guide.repository;

import com.festival.domain.guide.dto.GuideRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuideRepositoryCustom {
    Page<GuideRes> getList(String status, Pageable pageable);
}
