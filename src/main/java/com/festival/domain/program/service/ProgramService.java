package com.festival.domain.program.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.program.dto.*;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.repository.ProgramRepository;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.festival.common.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ImageService imageService;
    private final MemberService memberService;

    @Transactional
    public Long createProgram(ProgramReq programReq) {
        Program program = Program.of(programReq);
        program.setImage(imageService.createImage(programReq.getMainFile(), programReq.getSubFiles(), programReq.getType()));
        program.connectMember(memberService.getAuthenticationMember());

        return programRepository.save(program).getId();
    }

    @Transactional
    public Long updateProgram(Long programId, ProgramReq programReq) {
        Program program = checkingDeletedStatus(programRepository.findById(programId));

        if (!SecurityUtils.checkingRole(program.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(FORBIDDEN_UPDATE);
        }

        program.setImage(imageService.createImage(programReq.getMainFile(), programReq.getSubFiles(), programReq.getType()));
        program.update(programReq);
        return programId;
    }

    @Transactional
    public void deleteProgram(Long programId) {
        Program program = checkingDeletedStatus(programRepository.findById(programId));

        if (!SecurityUtils.checkingRole(program.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }

        program.changeStatus(OperateStatus.TERMINATE);
    }

    public ProgramRes getProgram(Long programId) {
        Program program = programRepository.findById(programId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PROGRAM));
        return ProgramRes.of(program);
    }

    public ProgramPageRes getProgramList(ProgramListReq programListReqDto, Pageable pageable) {
        return programRepository.getList(
                ProgramSearchCond.builder()
                .status(programListReqDto.getStatus())
                .type(programListReqDto.getType())
                .pageable(pageable)
                .build()
        );
    }

    public List<ProgramSearchRes> searchProgramList(String keyword) {
        return keyword.isEmpty() ? null : programRepository.searchProgramList(keyword);
    }

    private Program checkingDeletedStatus(Optional<Program> program) {
        if (program.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_PROGRAM);
        }
        if (program.get().getStatus() == OperateStatus.TERMINATE) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return program.get();
    }

}
