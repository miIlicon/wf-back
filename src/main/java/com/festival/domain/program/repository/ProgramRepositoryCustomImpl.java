package com.festival.domain.program.repository;


import com.festival.domain.program.model.Program;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.festival.domain.booth.model.QBooth.booth;
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
    public List<Program> getList(ProgramSearchCond programSearchCond, Pageable pageable) {
        return queryFactory
                .selectFrom(program)
                .join(program.image).fetchJoin()
                .where(
                        StatusEq(programSearchCond.getStatus()),
                        TypeEq(programSearchCond.getType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
