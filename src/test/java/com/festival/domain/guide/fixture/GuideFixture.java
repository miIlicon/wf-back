package com.festival.domain.guide.fixture;

import com.festival.common.base.OperateStatus;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.model.GuideType;

public class GuideFixture {

    public static final Guide DELETED_GUIDE = Guide.builder()
            .title("삭제된 가이드 제목")
            .content("삭제된 가이드 내용")
            .type(GuideType.NOTICE)
            .status(OperateStatus.TERMINATE)
            .build();
}
