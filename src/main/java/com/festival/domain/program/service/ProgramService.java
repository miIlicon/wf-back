package com.festival.domain.program.service;

import com.festival.domain.program.dto.ProgramListReq;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.model.ProgramStatus;
import com.festival.domain.program.repository.ProgramCustomRepository;
import com.festival.domain.program.repository.ProgramRepository;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final ProgramCustomRepository programCustomRepository;

    @Transactional
    public Long createProgram(ProgramReq programReqDto) {
        Program savedProgram = programRepository.save(Program.of(programReqDto));
        return savedProgram.getId();
    }

    public Long updateProgram(Long programId, ProgramReq programReqDto) {
        Program program = programRepository.findById(programId).orElseThrow();
        program.update(programReqDto);
        return programId;
    }

    public ProgramRes getProgram(Long programId) {
        Program program = programRepository.findById(programId).orElseThrow();
        return ProgramRes.builder()
                .title(program.getTitle())
                .subTitle(program.getSubTitle())
                .content(program.getContent())
                .type(program.getType().toString())
                .status(program.getStatus().toString())
                .longitude(program.getLongitude())
                .latitude(program.getLatitude())
                .build();

    }

    public List<ProgramRes> getProgramList(ProgramListReq programListReqDto, Pageable pageable) {
        return programCustomRepository.getList(new ProgramSearchCond(programListReqDto.getStatus(), programListReqDto.getType()), pageable);

    }

    @Transactional
    public void delete(Long programId) {
        Program program = programRepository.findById(programId).orElseThrow();
        program.setStatus(ProgramStatus.TERMINATE);
    }


}
