package com.festival.old.domain.foodTruck.repository.impl;

import com.festival.old.common.vo.SearchCond;
import com.festival.old.domain.foodTruck.data.entity.FoodTruck;
import com.festival.old.domain.foodTruck.repository.FoodTruckRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.old.domain.admin.data.entity.QAdmin.admin;
import static com.festival.old.domain.foodTruck.data.entity.QFoodTruck.foodTruck;
import static com.festival.old.domain.foodTruck.data.entity.QFoodTruckImage.foodTruckImage;

public class FoodTruckRepositoryCustomImpl implements FoodTruckRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FoodTruckRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private static BooleanExpression adminIdEq(SearchCond cond) {
        return admin.id.eq(cond.getUserId());
    }

    private static BooleanExpression stateEq(SearchCond cond) {
        return foodTruck.foodTruckState.eq(cond.getState());
    }

    @Override
    public Page<FoodTruck> findByIdTrucks(SearchCond cond, Pageable pageable) {
        List<FoodTruck> result = queryFactory
                .selectFrom(foodTruck)
                .leftJoin(foodTruck.foodTruckImage, foodTruckImage)
                .leftJoin(foodTruck.admin, admin)
                .where(
                        stateEq(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(foodTruck.count())
                .from(foodTruck)
                .leftJoin(foodTruck.admin, admin)
                .where(
                        stateEq(cond));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}