package com.festival.domain.info.festivalPub.repository;

import com.festival.common.vo.SearchCond;
import com.festival.domain.info.festivalPub.data.dto.request.PubSearchCond;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.data.dto.response.QPubResponse;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.admin.data.entity.QAdmin.*;
import static com.festival.domain.info.festivalPub.data.entity.file.QPubImage.*;
import static com.festival.domain.info.festivalPub.data.entity.pub.QPub.*;

public class PubRepositoryImpl implements PubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PubRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Pub> findByIdPubs(SearchCond cond, Pageable pageable) {
        List<Pub> result = queryFactory
                .selectFrom(pub)
                .leftJoin(pub.pubImage, pubImage)
                .leftJoin(pub.admin, admin)
                .where(
                        adminIdEq(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(pub.count())
                .from(pub)
                .leftJoin(pub.admin, admin)
                .where(
                        adminIdEq(cond));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Pub> findByIdPubsWithState(PubSearchCond cond, Pageable pageable) {
        List<Pub> result = queryFactory
                .selectFrom(pub)
                .leftJoin(pub.pubImage, pubImage)
                .leftJoin(pub.admin, admin)
                .where(
                        adminIdEq(cond),
                        stateEq(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(pub.count())
                .from(pub)
                .leftJoin(pub.admin, admin)
                .where(
                        adminIdEq(cond),
                        stateEq(cond));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private static BooleanExpression adminIdEq(SearchCond cond) {
        return admin.id.eq(cond.getUserId());
    }

    private static BooleanExpression adminIdEq(PubSearchCond cond) {
        return admin.id.eq(cond.getUserId());
    }

    private static BooleanExpression stateEq(PubSearchCond cond) {
        return pub.pubState.eq(cond.getState());
    }
}
