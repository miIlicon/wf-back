package com.festival.domain.program.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.program.dto.*;
import com.festival.domain.program.service.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/program")
public class ProgramController {

    private final ProgramService programService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @Operation(summary = "프로그램 등록", description = "MULTIPART_FORM_DATA로 보내주시면 감사하겠습니다.")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createProgram(@Valid @ParameterObject ProgramReq programReq) {
        validationUtils.isProgramValid(programReq);
        return ResponseEntity.ok().body(programService.createProgram(programReq, LocalDate.now()));
    }

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @Operation(summary = "프로그램 업데이트(전체) ",  description = "MULTIPART_FORM_DATA로 보내주시면 감사하겠습니다.")
    @PutMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateProgram(@PathVariable("programId") Long programId, @Valid @ParameterObject ProgramReq programReq) {
        validationUtils.isProgramValid(programReq);
        return ResponseEntity.ok().body(programService.updateProgram(programId, programReq));
    }

    @Operation(summary = "프로그램 운영 상태 업데이트")
    @PatchMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateProgramStatus(@PathVariable("programId") Long programId,
                                                    @NotNull @RequestParam(name = "status") String status) {
        return ResponseEntity.ok().body(programService.updateProgramStatus(programId, status));
    }

    @Operation(summary = "프로그램 삭제")
    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @DeleteMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteProgram(@PathVariable("programId") Long programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로그램 단건 조회")
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProgramRes> getProgram(@PathVariable("programId") Long programId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(programService.getProgram(programId, httpServletRequest.getRemoteAddr()));
    }

    @Operation(summary = "프로그램 목록 조회")
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProgramPageRes> getProgramList(@Valid @ParameterObject ProgramListReq programListReq) {
        return ResponseEntity.ok().body(programService.getProgramList(programListReq));
    }

    @Operation(summary = "프로그램 검색")
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProgramSearchRes>> searchProgramList(@RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok().body(programService.searchProgramList(keyword));
    }

}
