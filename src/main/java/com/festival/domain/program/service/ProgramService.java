package com.festival.domain.program.service;

import com.festival.domain.image.service.ImageService;
import com.festival.domain.program.dto.ProgramListReq;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.model.ProgramStatus;
import com.festival.domain.program.repository.ProgramRepositoryCustom;
import com.festival.domain.program.repository.ProgramRepository;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProgramService {
    private final ProgramRepository programRepository;
    private final ImageService imageService;

    @Transactional
    public Long createProgram(ProgramReq programReq) {
        Program program = Program.of(programReq);
        program.setImage(imageService.uploadImage(programReq.getMainFile(), programReq.getSubFiles(), programReq.getType()));
        return programRepository.save(program).getId();
    }

    @Transactional
    public Long updateProgram(Long programId, ProgramReq programReq) {
        Program program = programRepository.findById(programId).orElseThrow();
        program.setImage(imageService.uploadImage(programReq.getMainFile(), programReq.getSubFiles(), programReq.getType()));
        program.update(programReq);
        return programId;
    }

    public ProgramRes getProgram(Long programId) {
        Program program = programRepository.findById(programId).orElseThrow();
        return ProgramRes.of(program);

    }

    public List<ProgramRes> getProgramList(ProgramListReq programListReqDto, Pageable pageable) {
        List<Program> programList = programRepository.getList(ProgramSearchCond.builder()
                .status(programListReqDto.getStatus())
                .type(programListReqDto.getType())
                .build(), pageable);
        return programList.stream().map(ProgramRes::of).collect(Collectors.toList());

    }

    @Transactional
    public void delete(Long programId) {
        Program program = programRepository.findById(programId).orElseThrow();
        program.setStatus(ProgramStatus.TERMINATE);
    }


}
