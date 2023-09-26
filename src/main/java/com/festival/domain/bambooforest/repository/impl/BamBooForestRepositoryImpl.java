package com.festival.domain.bambooforest.repository.impl;

import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.model.BamBooForest;
import com.festival.domain.bambooforest.repository.BamBooForestRepositoryCustom;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.bambooforest.model.QBamBooForest.bamBooForest;

public class BamBooForestRepositoryImpl implements BamBooForestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BamBooForestRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public BamBooForestPageRes getList(Pageable pageable) {
        List<BamBooForest> result = queryFactory
                .selectFrom(bamBooForest)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(bamBooForest.deleted.eq(false))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(bamBooForest.count())
                .where(bamBooForest.deleted.eq(false))
                .from(bamBooForest);

        Page<BamBooForest> page = PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
        return BamBooForestPageRes.of(page);
    }

}
