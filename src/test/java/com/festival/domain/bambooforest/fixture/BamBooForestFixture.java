package com.festival.domain.bambooforest.fixture;


import com.festival.domain.bambooforest.model.BamBooForest;

public class BamBooForestFixture {

    public static BamBooForest bamBooForest = BamBooForest.builder()
            .content("대나무 숲 글 내용")
            .contact("010-1234-5678")
            .bamBooForestStatus(BamBooForestStatus.OPERATE)
            .build();
}

