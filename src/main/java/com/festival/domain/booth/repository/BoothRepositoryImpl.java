package com.festival.domain.booth.repository;

import com.festival.domain.booth.model.Booth;
import com.festival.domain.booth.model.BoothType;
import com.festival.domain.booth.service.vo.BoothListSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.festival.domain.booth.model.QBooth.booth;


public class BoothRepositoryImpl implements BoothRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoothRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Booth> getList(BoothListSearchCond boothListSearchCond, Pageable pageable) {
        return queryFactory.selectFrom(booth)
                .join(booth.image).fetchJoin()
                .where(
                        eqStatus(boothListSearchCond.getStatus()),
                        eqType(boothListSearchCond.getType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        
    }

    private static BooleanExpression eqType(String type) {
        return type == null ? null : booth.type.eq(BoothType.valueOf(type));
    }

    private static BooleanExpression eqStatus(String status) {
        return status == null ? null : booth.status.stringValue().eq(status);
    }
}
