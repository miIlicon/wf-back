package com.festival.domain.bambooforest.repository.impl;

import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepositoryCustom;
import com.festival.domain.bambooforest.service.vo.BamBooForestSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.bambooforest.model.QBamBooForest.bamBooForest;

public class BamBooForestRepositoryImpl implements BamBooForestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BamBooForestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public BamBooForestPageRes getList(BamBooForestSearchCond bamBooForestSearchCond) {
        List<BamBooForest> result = queryFactory
                .selectFrom(bamBooForest)
                .where(
                        statusEq(bamBooForestSearchCond.getStatus())
                )
                .offset(bamBooForestSearchCond.getPageable().getOffset())
                .limit(bamBooForestSearchCond.getPageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(bamBooForest.count())
                .from(bamBooForest)
                .where(
                        statusEq(bamBooForestSearchCond.getStatus())
                );
        Page<BamBooForest> page = PageableExecutionUtils.getPage(result, bamBooForestSearchCond.getPageable(), countQuery::fetchOne);
        return BamBooForestPageRes.of(page);
    }

    private static BooleanExpression statusEq(String status) {
        return bamBooForest.status.stringValue().eq(status);
    }
}
