package com.festival.domain.program.controller;


import com.festival.domain.program.dto.ProgramListReq;
import com.festival.domain.program.dto.ProgramListRes;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.program.dto.ProgramRes;
import com.festival.domain.program.service.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/program")
public class ProgramController {
    private final ProgramService programService;


    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid ProgramReq programReqDto) {
        return ResponseEntity.ok().body(programService.createProgram(programReqDto));
    }

    @PutMapping("/{programId}")
    public ResponseEntity<Long> update(@PathVariable Long programId, @RequestBody @Valid ProgramReq programReqDto) {
        return ResponseEntity.ok().body(programService.updateProgram(programId, programReqDto));
    }

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramRes> get(@PathVariable Long programId) {
        return ResponseEntity.ok().body(programService.getProgram(programId));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProgramListRes>> getList(@RequestBody ProgramListReq programListReqDto, Pageable pageable) {
        return ResponseEntity.ok().body(programService.getProgramList(programListReqDto, pageable));
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> delete(@PathVariable Long programId) {
        programService.delete(programId);
        return ResponseEntity.ok().build();
    }

}
