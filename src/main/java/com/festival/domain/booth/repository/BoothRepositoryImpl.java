package com.festival.domain.booth.repository;

import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.booth.model.QBooth.booth;

public class BoothRepositoryImpl implements BoothRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoothRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public BoothPageRes getList(BoothListSearchCond boothListSearchCond) {
        List<Booth> result = queryFactory
                .selectFrom(booth)
                .join(booth.image).fetchJoin()
                .where(
                        eqStatus(boothListSearchCond.getStatus()),
                        eqType(boothListSearchCond.getType())
                )
                .offset(boothListSearchCond.getPageable().getOffset())
                .limit(boothListSearchCond.getPageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(booth.count())
                .from(booth)
                .where(
                        eqStatus(boothListSearchCond.getStatus()),
                        eqType(boothListSearchCond.getType())
                );

        Page<Booth> page = PageableExecutionUtils.getPage(result, boothListSearchCond.getPageable(), countQuery::fetchOne);
        return BoothPageRes.of(page);
    }

    private static BooleanExpression eqType(String type) {
        return type == null ? null : booth.type.eq(BoothType.valueOf(type));
    }
    private static BooleanExpression statusEq(String status) {
        return booth.status.stringValue().eq(status);
    }
    private static BooleanExpression eqStatus(String status) {
        return status == null ? null : booth.status.stringValue().eq(status);
    }
}
