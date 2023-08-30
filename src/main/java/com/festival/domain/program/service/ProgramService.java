package com.festival.domain.program.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
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
    public Long updateProgram(Long programId, ProgramReq programReq, String username) {
        Program program = programRepository.findById(programId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PROGRAM));
        if (program.getLastModifiedBy().equals(username)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_UPDATE);
        }
        program.setImage(imageService.uploadImage(programReq.getMainFile(), programReq.getSubFiles(), programReq.getType()));
        program.update(programReq);
        return programId;
    }

    @Transactional
    public void delete(Long programId, String username) {
        Program program = programRepository.findById(programId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PROGRAM));
        if (program.getLastModifiedBy().equals(username)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }
        program.setStatus(ProgramStatus.TERMINATE);
    }

    public List<ProgramRes> getProgramList(ProgramListReq programListReqDto, Pageable pageable) {
        List<Program> programList = programRepository.getList(ProgramSearchCond.builder()
                .status(programListReqDto.getStatus())
                .type(programListReqDto.getType())
                .build(), pageable);
        return programList.stream().map(ProgramRes::of).collect(Collectors.toList());

    }

    public List<ProgramRes> getProgramList(ProgramListReq programListReqDto, Pageable pageable) {
        List<Program> programList = programRepository.getList(ProgramSearchCond.builder()
                .status(programListReqDto.getStatus())
                .type(programListReqDto.getType())
                .build(), pageable);
        return programList.stream().map(ProgramRes::of).collect(Collectors.toList());

    }


}
