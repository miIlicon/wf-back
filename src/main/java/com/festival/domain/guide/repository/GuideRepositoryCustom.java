package com.festival.domain.guide.repository;

import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.repository.vo.GuideSearchCond;

public interface GuideRepositoryCustom {
    GuidePageRes getList(GuideSearchCond guideSearchCond);
}
