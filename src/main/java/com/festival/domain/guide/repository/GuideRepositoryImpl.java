package com.festival.domain.guide.repository;

import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.dto.QGuideRes;
import com.festival.domain.guide.model.Guide;
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
    public List<Guide> getList(String status, Pageable pageable) {
        return  queryFactory
                .selectFrom(guide)
                .where(
                        statusEq(status)
                )
                .fetch();
    }

    private static BooleanExpression statusEq(String status) {
        return status == null ? null : guide.guideStatus.stringValue().eq(status);
    }
}
