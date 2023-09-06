package com.festival.domain.guide.repository.impl;

import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.guide.repository.GuideRepositoryCustom;
import com.festival.domain.guide.repository.vo.GuideSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.guide.model.QGuide.guide;

public class GuideRepositoryImpl implements GuideRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GuideRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public GuidePageRes getList(GuideSearchCond guideSearchCond) {
        List<Guide> result = queryFactory
                .selectFrom(guide)
                .where(
                        statusEq(guideSearchCond.getStatus())
                )
                .offset(guideSearchCond.getPageable().getOffset())
                .limit(guideSearchCond.getPageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(guide.count())
                .from(guide)
                .where(
                        statusEq(guideSearchCond.getStatus())
                );
        Page<Guide> page = PageableExecutionUtils.getPage(result, guideSearchCond.getPageable(), countQuery::fetchOne);
        return GuidePageRes.of(page);
    }

    private static BooleanExpression statusEq(String status) {
        return status == null ? null : guide.status.stringValue().eq(status);
    }
}