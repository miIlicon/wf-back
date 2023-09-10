package com.festival.domain.program.repository;

import com.festival.domain.program.dto.ProgramPageRes;
import com.festival.domain.program.dto.ProgramSearchRes;
import com.festival.domain.program.service.vo.ProgramSearchCond;

import java.util.List;

public interface ProgramRepositoryCustom {
    ProgramPageRes getList(ProgramSearchCond programSearchCond);

    List<ProgramSearchRes> searchProgramList(String keyword);
}
