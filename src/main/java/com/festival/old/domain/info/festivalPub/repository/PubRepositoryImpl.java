/*
package com.festival.old.domain.info.festivalPub.repository;

import com.festival.old.common.vo.SearchCond;
import com.festival.old.domain.info.festivalPub.data.entity.pub.Pub;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static old.domain.admin.data.entity.QAdmin.admin;
import static old.domain.info.festivalPub.data.entity.file.QPubImage.pubImage;
import static old.domain.info.festivalPub.data.entity.pub.QPub.pub;


public class PubRepositoryImpl implements PubRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PubRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private static BooleanExpression adminIdEq(SearchCond cond) {
        return admin.id.eq(cond.getUserId());
    }

    private static BooleanExpression stateEq(SearchCond cond) {
        return pub.pubState.eq(cond.getState());
    }

    @Override
    public Page<Pub> findByIdPubs(SearchCond cond, Pageable pageable) {
        List<Pub> result = queryFactory
                .selectFrom(pub)
                .leftJoin(pub.pubImage, pubImage)
                .leftJoin(pub.admin, admin)
                .where(
                        stateEq(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(pub.count())
                .from(pub)
                .leftJoin(pub.admin, admin)
                .where(
                        stateEq(cond));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
*/
