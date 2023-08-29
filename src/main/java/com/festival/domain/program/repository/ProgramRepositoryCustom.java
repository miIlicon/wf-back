package com.festival.domain.program.repository;

import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramRepositoryCustom {
    List<Program> getList(ProgramSearchCond programSearchCond, Pageable pageable);
}
