package com.festival.domain.program.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.program.dto.*;
import com.festival.domain.program.service.ProgramService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/program")
public class ProgramController {

    private final ProgramService programService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasAuthority({'ADMIN', 'MANAGER'})")
    @PostMapping
    public ResponseEntity<Long> createProgram(@Valid ProgramReq programReqDto) throws Exception {
        if (!validationUtils.isProgramValid(programReqDto)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(programService.createProgram(programReqDto));
    }

    @PreAuthorize("hasAuthority({'ADMIN', 'MANAGER'})")
    @PutMapping("/{programId}")
    public ResponseEntity<Long> updateProgram(@PathVariable Long programId,
                                       @Valid ProgramReq programReqDto) throws Exception {
        if (!validationUtils.isProgramValid(programReqDto)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(programService.updateProgram(programId, programReqDto));
    }

    @PreAuthorize("hasAuthority({'ADMIN', 'MANAGER'})")
    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long programId) {
        programService.deleteProgram(programId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{programId}")
    public ResponseEntity<ProgramRes> getProgram(@PathVariable Long programId) {
        return ResponseEntity.ok().body(programService.getProgram(programId));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public ResponseEntity<ProgramPageRes> getProgramList(@Valid ProgramListReq programListReqDto, Pageable pageable) {
        return ResponseEntity.ok().body(programService.getProgramList(programListReqDto, pageable));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/search")
    public ResponseEntity<List<ProgramSearchRes>> searchProgramList(@RequestParam(name = "keyword") String keyword) {
        return ResponseEntity.ok().body(programService.searchProgramList(keyword));
    }

}
