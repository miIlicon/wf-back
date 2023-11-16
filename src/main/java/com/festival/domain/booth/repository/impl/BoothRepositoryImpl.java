package com.festival.domain.booth.repository.impl;

import com.festival.common.base.OperateStatus;
import com.festival.domain.booth.controller.dto.BoothPageRes;
import com.festival.domain.booth.controller.dto.BoothSearchRes;
import com.festival.domain.booth.controller.dto.QBoothSearchRes;
import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.repository.BoothRepositoryCustom;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.booth.model.QBooth.booth;

public class BoothRepositoryImpl implements BoothRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final OrderSpecifier<Integer> operateStatusAsc = new CaseBuilder()  // 정렬 조건
            .when(booth.operateStatus.stringValue().eq("OPERATE")).then(1)
            .when(booth.operateStatus.stringValue().eq("UPCOMING")).then(2)
            .otherwise(3)
            .asc();

    public BoothRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public BoothPageRes getBoothList(BoothListSearchCond boothListSearchCond) {
        List<Booth> result = queryFactory
                .selectFrom(booth)
                .join(booth.image).fetchJoin()
                .where(
                        eqType(boothListSearchCond.getType()),
                        booth.deleted.eq(false)
                )
                .offset(boothListSearchCond.getPageable().getOffset())
                .limit(boothListSearchCond.getPageable().getPageSize())
                .orderBy(booth.viewCount.desc(), operateStatusAsc)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(booth.count())
                .from(booth)
                .where(
                        eqType(boothListSearchCond.getType()),
                        booth.deleted.eq(false)
                );

        /**
         * page 정보를 담기 위함
         */
        Page<Booth> page = PageableExecutionUtils.getPage(result, boothListSearchCond.getPageable(), countQuery::fetchOne);
        return BoothPageRes.of(page);
    }

    @Override
    public List<BoothSearchRes> searchBoothList(String keyword) {
       return queryFactory
               .select(new QBoothSearchRes(
                        booth.id,
                        booth.title,
                        booth.subTitle,
                        booth.operateStatus.stringValue(),
                        booth.image.mainFilePath
               ))
               .from(booth)
               .where(
                       eqKeyword(keyword),
                       booth.deleted.eq(false),
                       booth.operateStatus.eq(OperateStatus.OPERATE)
               )
               .orderBy(booth.viewCount.desc(), operateStatusAsc)
               .fetch();
    }

    private static BooleanExpression eqKeyword(String keyword) {
        return booth.title.contains(keyword).or(booth.subTitle.contains(keyword));
    }

    private static BooleanExpression eqType(String type) {
        return type == null ? null : booth.type.eq(BoothType.valueOf(type));
    }
}
