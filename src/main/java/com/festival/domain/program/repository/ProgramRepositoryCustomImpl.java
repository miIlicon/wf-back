package com.festival.domain.program.repository;

import com.festival.domain.program.dto.ProgramPageRes;
import com.festival.domain.program.dto.ProgramSearchRes;
import com.festival.domain.program.dto.QProgramSearchRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.festival.domain.image.model.QImage.image;
import static com.festival.domain.program.model.QProgram.program;

public class ProgramRepositoryCustomImpl implements ProgramRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProgramRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private static BooleanExpression StatusEq(String status) {
        return status == null ? null : program.status.stringValue().eq(status);
    }

    private static BooleanExpression TypeEq(String type) {
        return type == null ? null : program.type.stringValue().eq(type);
    }

    @Override
    public ProgramPageRes getList(ProgramSearchCond programSearchCond) {
        List<Program> result = queryFactory
                .selectFrom(program)
                .join(program.image).fetchJoin()
                .where(
                        StatusEq(programSearchCond.getStatus()),
                        TypeEq(programSearchCond.getType())
                )
                .offset(programSearchCond.getPageable().getOffset())
                .limit(programSearchCond.getPageable().getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(program.count())
                .from(program)
                .where(
                        StatusEq(programSearchCond.getStatus()),
                        TypeEq(programSearchCond.getType())
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
                        program.status.stringValue(),
                        program.image.mainFilePath
                ))
                .from(program)
                .where(
                        keywordEqTitleOrSubTitle(keyword)
                )
                .fetch();
    }

    private static BooleanExpression keywordEqTitleOrSubTitle(String keyword) {
        return program.title.contains(keyword)
                .or(program.subTitle.contains(keyword));
    }

}
