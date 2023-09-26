package com.festival.domain.guide.fixture;

import com.festival.domain.guide.model.Guide;

public class GuideFixture {

    public static final Guide DELETED_GUIDE = Guide.builder()
            .title("삭제된 가이드 제목")
            .content("삭제된 가이드 내용")
            .build();
}
