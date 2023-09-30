package com.festival.domain.booth.fixture;

import com.festival.common.base.OperateStatus;
import com.festival.domain.booth.model.Booth;

import com.festival.domain.booth.model.BoothType;

public class BoothFixture {

    public static Booth FOOD_TRUCK = Booth.builder()
            .title("푸드트럭 게시물 제목")
            .subTitle("푸드트럭 게시물 부제목")
            .operateStatus(OperateStatus.OPERATE)
            .content("푸드트럭 게시물 내용")
            .longitude(50)
            .latitude(50)
            .type(BoothType.FOOD_TRUCK)
            .build();


    public static Booth PUB = Booth.builder()
            .title("주점 게시물 제목")
            .subTitle("주점 게시물 부제목")
            .operateStatus(OperateStatus.OPERATE)
            .content("주점 게시물 내용")
            .longitude(50)
            .latitude(50)
            .type(BoothType.PUB)
            .build();

    public static final Booth FLEA_MARKET = Booth.builder()
            .title("플리마켓 게시물 제목")
            .subTitle("플리마켓 게시물 부제목")
            .operateStatus(OperateStatus.OPERATE)
            .content("플리마켓 게시물 내용")
            .longitude(50)
            .latitude(50)
            .type(BoothType.FLEA_MARKET)
            .build();

    public static final Booth DELETED_BOOTH = Booth.builder()
            .title("삭제된 부스")
            .subTitle("삭제된 부스 부제목")
            .operateStatus(OperateStatus.TERMINATE)
            .deleted(true)
            .content("삭제된 부스 내용")
            .longitude(50)
            .latitude(50)
            .type(BoothType.FOOD_TRUCK)
            .build();
}
