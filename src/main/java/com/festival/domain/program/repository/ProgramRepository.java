package com.festival.domain.program.repository;

import com.festival.common.base.OperateStatus;
import com.festival.domain.program.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> , ProgramRepositoryCustom{
    List<Program> findByStartDateEqualsAndOperateStatusEquals(LocalDate startDate, OperateStatus operateStatus);
    List<Program> findByEndDateEquals(LocalDate currentDate);
}
