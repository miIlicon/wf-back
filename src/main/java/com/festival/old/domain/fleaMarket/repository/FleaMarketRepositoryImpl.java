package com.festival.old.domain.fleaMarket.repository;

import com.festival.old.common.vo.SearchCond;
import com.festival.old.domain.fleaMarket.data.entity.FleaMarket;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.old.domain.admin.data.entity.QAdmin.admin;
import static com.festival.old.domain.fleaMarket.data.entity.QFleaMarket.fleaMarket;
import static com.festival.old.domain.fleaMarket.data.entity.QFleaMarketImage.fleaMarketImage;

public class FleaMarketRepositoryImpl implements FleaMarketRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FleaMarketRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private static BooleanExpression adminIdEq(SearchCond cond) {
        return admin.id.eq(cond.getUserId());
    }

    private static BooleanExpression stateEq(SearchCond cond) {
        return fleaMarket.fleaMarketState.eq(cond.getState());
    }

    @Override
    public Page<FleaMarket> findByIdFleaMarkets(SearchCond cond, Pageable pageable) {
        List<FleaMarket> result = queryFactory
                .selectFrom(fleaMarket)
                .leftJoin(fleaMarket.fleaMarketImage, fleaMarketImage)
                .leftJoin(fleaMarket.admin, admin)
                .where(
                        stateEq(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(fleaMarket.count())
                .from(fleaMarket)
                .leftJoin(fleaMarket.admin, admin)
                .where(
                        stateEq(cond));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
