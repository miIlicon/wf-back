package com.festival.domain.bambooforest.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.bambooforest.dto.BamBooForestListReq;
import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.service.BamBooForestService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/bambooforest")
public class BambooForestController {

    private final BamBooForestService bambooForestService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("permitAll()")
    @Operation(summary = "대나무숲 글 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createBamBooForest(@Valid BamBooForestReq bamBooForestReq) {
        //validationUtils.isBamBooForestValid(bamBooForestReq);
        return ResponseEntity.ok().body(bambooForestService.createBamBooForest(bamBooForestReq));
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @Operation(summary = "대나무숲 글 삭제 등록")
    @DeleteMapping(value = "/{bamBooForestId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBamBooForest(@PathVariable("bamBooForestId") Long bamBooForestId) {
        log.info("deleteController");
        bambooForestService.deleteBamBooForest(bamBooForestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "대나무숲 목록 조회")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BamBooForestPageRes> getBamBooForestList(@Valid @ParameterObject BamBooForestListReq bamBooForestListReq) {
        return ResponseEntity.ok().body(bambooForestService.getBamBooForestList(PageRequest.of(bamBooForestListReq.getPage(), bamBooForestListReq.getSize())));
    }
}
