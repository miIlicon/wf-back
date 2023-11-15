package com.festival.domain.program.repository.impl;

import com.festival.common.base.OperateStatus;
import com.festival.domain.program.dto.ProgramPageRes;
import com.festival.domain.program.dto.ProgramSearchRes;
import com.festival.domain.program.dto.QProgramSearchRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.repository.ProgramRepositoryCustom;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.program.model.QProgram.program;

public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final OrderSpecifier<Integer> operateStatusASC = new CaseBuilder()
            .when(program.operateStatus.stringValue().eq("OPERATE")).then(1)
            .when(program.operateStatus.stringValue().eq("UPCOMING")).then(2)
            .otherwise(3)
            .asc();

    public ProgramRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public ProgramPageRes getProgramList(ProgramSearchCond programSearchCond) {
        List<Program> result = queryFactory
                .selectFrom(program)
                .join(program.image).fetchJoin()
                .where(
                        TypeEq(programSearchCond.getType()),
                        program.deleted.eq(false)
                )
                .offset(programSearchCond.getPageable().getOffset())
                .limit(programSearchCond.getPageable().getPageSize())
                .orderBy(operateStatusASC, program.viewCount.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(program.count())
                .from(program)
                .where(
                        TypeEq(programSearchCond.getType()),
                        program.deleted.eq(false)
                );

        Page<Program> page = PageableExecutionUtils.getPage(result, programSearchCond.getPageable(), countQuery::fetchOne);
        return ProgramPageRes.of(page);
    }

    @Override
    public List<ProgramSearchRes> searchProgramList(String keyword) {
        return queryFactory
                .select(new QProgramSearchRes(
                        program.id,
                        program.title,
                        program.subTitle,
                        program.operateStatus.stringValue(),
                        program.image.mainFilePath
                ))
                .from(program)
                .where(
                        keywordEqTitleOrSubTitle(keyword),
                        program.deleted.eq(false),
                        program.operateStatus.eq(OperateStatus.OPERATE)
                )
                .orderBy(operateStatusASC, program.viewCount.desc())
                .fetch();
    }

    private static BooleanExpression TypeEq(String type) {
        return type == null ? null : program.type.stringValue().eq(type);
    }

    private static BooleanExpression keywordEqTitleOrSubTitle(String keyword) {
        return program.title.contains(keyword)
                .or(program.subTitle.contains(keyword));
    }

}
