package com.festival.domain.program.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.program.dto.*;
import com.festival.domain.program.service.ProgramService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/program")
public class ProgramController {

    private final ProgramService programService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createProgram(@Valid ProgramReq programReq) {
        validationUtils.isProgramValid(programReq);
        return ResponseEntity.ok().body(programService.createProgram(programReq));
    }

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @PutMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateProgram(@PathVariable Long programId, @Valid ProgramReq programReq) {
        validationUtils.isProgramValid(programReq);
        return ResponseEntity.ok().body(programService.updateProgram(programId, programReq));
    }

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @DeleteMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteProgram(@PathVariable Long programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{programId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProgramRes> getProgram(@PathVariable Long programId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(programService.getProgram(programId, httpServletRequest.getRemoteAddr()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ProgramPageRes> getProgramList(@Valid @RequestBody ProgramListReq programListReq) {
        return ResponseEntity.ok().body(programService.getProgramList(programListReq));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProgramSearchRes>> searchProgramList(@RequestBody String keyword) {
        return ResponseEntity.ok().body(programService.searchProgramList(keyword));
    }

}
