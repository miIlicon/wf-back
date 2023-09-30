package com.festival.domain.guide.repository;

import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.repository.vo.GuideSearchCond;
import org.springframework.data.domain.Pageable;

public interface GuideRepositoryCustom {
    GuidePageRes getList(Pageable pageable);
}
