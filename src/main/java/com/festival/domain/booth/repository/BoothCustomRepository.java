package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.controller.dto.QBoothRes;
import com.festival.domain.booth.model.BoothStatus;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;

import static com.festival.domain.booth.model.QBooth.booth;

@RequiredArgsConstructor
@Repository
public class BoothCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<BoothRes> getList(BoothListSearchCond boothListSearchCond, Pageable pageable) {
        return queryFactory.select(new QBoothRes(
                        booth.title,
                booth.subTitle,
                booth.content,
                booth.latitude,
                booth.longitude,
                booth.status.stringValue(),
                booth.type.stringValue()
                ))
                .from(booth)
                .where(
                        eqStatus(boothListSearchCond.getStatus()),
                        eqType(boothListSearchCond.getType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

    private static BooleanExpression eqType(String type) {
        return type == null ? null : booth.type.eq(BoothType.valueOf(type));
    }

    private static BooleanExpression eqStatus(String status) {
        return status == null ? null : booth.status.eq(BoothStatus.valueOf(status));
    }
}
