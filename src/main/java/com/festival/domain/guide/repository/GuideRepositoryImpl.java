package com.festival.domain.guide.repository;

import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.dto.QGuideRes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.guide.model.QGuide.guide;

public class GuideRepositoryImpl implements GuideRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public GuideRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GuideRes> getList(String status, Pageable pageable) {
        List<GuideRes> result = queryFactory
                .select(new QGuideRes(
                        guide.id,
                        guide.title
                ))
                .from(guide)
                .where(
                        statusEq(status)
                )
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(guide.count())
                .from(guide)
                .where(
                        statusEq(status)
                );

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private static BooleanExpression statusEq(String status) {
        return status == null ? null : guide.guideStatus.stringValue().eq(status);
    }
}
