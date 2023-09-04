package com.festival.domain.program.repository;

import com.festival.domain.program.dto.ProgramPageRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.service.vo.ProgramSearchCond;

import java.util.List;

public interface ProgramRepositoryCustom {
    ProgramPageRes getList(ProgramSearchCond programSearchCond);
}
