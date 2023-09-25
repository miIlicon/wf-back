package com.festival.domain.program.service;

import com.festival.common.base.OperateStatus;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.AlreadyDeleteException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.program.dto.*;
import com.festival.domain.program.model.Program;
import com.festival.domain.program.repository.ProgramRepository;
import com.festival.domain.program.service.vo.ProgramSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final RedisService redisService;

    @Transactional
    public Long createProgram(ProgramReq programReq, LocalDate dateTime) {
        Program program = Program.of(programReq, dateTime);
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
        return program.getId();
    }

    @Transactional
    public Long updateProgramStatus(Long programId, String status) {
        Program program = checkingDeletedStatus(programRepository.findById(programId));

        if (!SecurityUtils.checkingRole(program.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(FORBIDDEN_UPDATE);
        }

        program.changeStatus(status);
        return program.getId();
    }

    @Transactional
    public void settingProgramStatus() {
        LocalDate registeredDate = LocalDate.now();
        programRepository.findByStartDateEqualsAndOperateStatusEquals(registeredDate, OperateStatus.UPCOMING.getValue())
                .forEach(p -> p.changeStatus(OperateStatus.OPERATE.getValue()));

        programRepository.findByEndDateEquals(registeredDate)
                .forEach(p -> p.changeStatus(OperateStatus.TERMINATE.getValue()));
    }

    @Transactional
    public void deleteProgram(Long programId) {
        Program program = checkingDeletedStatus(programRepository.findById(programId));

        if (!SecurityUtils.checkingRole(program.getMember(), memberService.getAuthenticationMember())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);
        }

        program.deletedProgram();
    }

    public ProgramRes getProgram(Long programId, String ipAddress) {
        Program program = programRepository.findById(programId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PROGRAM));
        if(redisService.isDuplicateAccess(ipAddress, "Program_" + program.getId())) {
            redisService.increaseRedisViewCount("Program_Id_" + program.getId());
        }
        return ProgramRes.of(program);
    }

    public ProgramPageRes getProgramList(ProgramListReq programListReq) {
        return programRepository.getList(
                ProgramSearchCond.builder()
                .type(programListReq.getType())
                .pageable(PageRequest.of(programListReq.getPage(), programListReq.getSize()))
                .build()
        );
    }

    public List<ProgramSearchRes> searchProgramList(String keyword) {
        return programRepository.searchProgramList(keyword);
    }

    @Transactional
    public void increaseProgramViewCount(Long id, Long viewCount) {
        Program program = programRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM));
        program.increaseViewCount(viewCount);
    }

    private Program checkingDeletedStatus(Optional<Program> program) {
        if (program.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_PROGRAM);
        }
        if (program.get().isDeleted()) {
            throw new AlreadyDeleteException(ALREADY_DELETED);
        }
        return program.get();
    }
}
