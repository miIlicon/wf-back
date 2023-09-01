package com.festival.domain.program.service;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.util.SecurityUtils;
import com.festival.domain.guide.model.Guide;
import com.festival.domain.image.service.ImageService;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.service.MemberService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.festival.common.exception.ErrorCode.ALREADY_DELETED;
import static com.festival.common.exception.ErrorCode.NOT_FOUND_GUIDE;
import static com.festival.domain.guide.model.GuideStatus.TERMINATE;

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
        return programRepository.save(program).getId();
    }

    @Transactional
    public Long updateProgram(Long programId, ProgramReq programReq) {
        Program program = checkingDeletedStatus(programRepository.findById(programId));
        Member findMember = memberService.getAuthenticationMember();
        if(!SecurityUtils.checkingRole(findMember.getUsername(), program.getMember().getUsername(), findMember.getMemberRoles()))
            throw new ForbiddenException(ErrorCode.FORBIDDEN_UPDATE);

        program.setImage(imageService.createImage(programReq.getMainFile(), programReq.getSubFiles(), programReq.getType()));
        program.update(programReq);
        return programId;
    }

    @Transactional
    public void delete(Long programId) {
        Program program = checkingDeletedStatus(programRepository.findById(programId));
        Member findMember = memberService.getAuthenticationMember();
        if(!SecurityUtils.checkingRole(findMember.getUsername(), program.getMember().getUsername(), findMember.getMemberRoles()))
            throw new ForbiddenException(ErrorCode.FORBIDDEN_DELETE);

        program.setStatus(ProgramStatus.TERMINATE);
    }

    public ProgramRes getProgram(Long programId) {
        Program program = programRepository.findById(programId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PROGRAM));
        return ProgramRes.of(program);

    }

    public List<ProgramRes> getProgramList(ProgramListReq programListReqDto, Pageable pageable) {
        List<Program> programList = programRepository.getList(ProgramSearchCond.builder()
                .status(programListReqDto.getStatus())
                .type(programListReqDto.getType())
                .build(), pageable);
        return programList.stream().map(ProgramRes::of).collect(Collectors.toList());

    }
    private Program checkingDeletedStatus(Optional<Program> program) {
        if (program.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_GUIDE);
        }
        if (program.get().getStatus().equals(TERMINATE)) {
            throw new BadRequestException(ALREADY_DELETED);
        }
        return program.get();
    }
}
