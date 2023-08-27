package com.festival.domain.program.repository;

import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.program.dto.QProgramRes;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.festival.domain.program.model.QProgram.program;

public class ProgramCustomRepositoryImpl implements ProgramCustomRepository {

    private JPAQueryFactory queryFactory;

    public ProgramCustomRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private static BooleanExpression StatusEq(String status) {
        return status == null ? null : program.status.stringValue().eq(status);
    }

    private static BooleanExpression TypeEq(String type) {
        return type == null ? null : program.type.stringValue().eq(type);
    }

    @Override
    public List<ProgramRes> getList(ProgramSearchCond programSearchCond, Pageable pageable) {
        return queryFactory
                .select(new QProgramRes(
                        program.title,
                        program.subTitle,
                        program.content,
                        program.latitude,
                        program.longitude,
                        program.status.stringValue(),
                        program.type.stringValue()
                ))
                .from(program)
                .where(
                        StatusEq(programSearchCond.getStatus()),
                        TypeEq(programSearchCond.getType())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
