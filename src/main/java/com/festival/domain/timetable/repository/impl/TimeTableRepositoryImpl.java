package com.festival.domain.timetable.repository.impl;

import com.festival.domain.timetable.dto.QTimeTableRes;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.dto.TimeTableSearchCond;
import com.festival.domain.timetable.repository.TimeTableRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.festival.domain.timetable.model.QTimeTable.*;

public class TimeTableRepositoryImpl implements TimeTableRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TimeTableRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<TimeTableRes> getList(TimeTableSearchCond timeTableSearchCond) {
        return queryFactory
                .select(new QTimeTableRes(
                        timeTable.title
                ))
                .from(timeTable)
                .where(
                        isWithinPeriod(timeTableSearchCond.getStartTime(), timeTableSearchCond.getEndTime()),
                        StatusEq(timeTableSearchCond.getStatus())
                )
                .fetch();
    }

    private static BooleanExpression isWithinPeriod(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return null;
        }
        return timeTable.startTime.after(startTime)
                .and(timeTable.endTime.before(endTime));
    }

    private static BooleanExpression StatusEq(String status) {
        return status == null ? null : timeTable.timeTableStatus.stringValue().eq(status);
    }
}
