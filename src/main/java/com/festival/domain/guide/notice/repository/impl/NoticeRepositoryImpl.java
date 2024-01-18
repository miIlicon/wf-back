package com.festival.domain.guide.notice.repository.impl;

import com.festival.domain.guide.notice.dto.NoticePageRes;
import com.festival.domain.guide.notice.model.Notice;
import com.festival.domain.guide.notice.repository.NoticeRepositoryCustom;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.guide.notice.model.QNotice.notice;

public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NoticeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public NoticePageRes getList(Pageable pageable) {
        EntityPath<?> QNotice = null;
        List<Notice> result = queryFactory
                .selectFrom(notice)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(notice.deleted.eq(false))
                .orderBy(notice.id.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(notice.count())
                .where(notice.deleted.eq(false))
                .from(notice);

        Page<Notice> page = PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
        return NoticePageRes.of(page);
    }

}
