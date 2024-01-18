package com.festival.domain.guide.notice.fixture;

import com.festival.domain.guide.notice.model.Notice;

public class GuideFixture {

    public static final Notice DELETED_NOTICE = Notice.builder()
            .content("삭제된 가이드 내용")
            .deleted(true)
            .build();
}
