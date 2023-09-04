package com.festival.domain.bambooforest.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.bambooforest.dto.BamBooForestPageRes;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.bambooforest.service.BamBooForestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/bambooforest")
public class BambooForestController {

    private final BamBooForestService bambooForestService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<Long> createBamBooForest(@Valid BamBooForestReq bamBooForestReq) throws Exception {
        if (!validationUtils.isBamBooForestValid(bamBooForestReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(bambooForestService.createBamBooForest(bamBooForestReq));
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @DeleteMapping("/{bamBooForestId}")
    public ResponseEntity<Void> deleteBamBooForest(@PathVariable Long bamBooForestId) {
        bambooForestService.deleteBamBooForest(bamBooForestId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public ResponseEntity<BamBooForestPageRes> getBamBooForestList(@NotNull(message = "상태값을 입력해주세요.") String status, Pageable pageable) {
        return ResponseEntity.ok().body(bambooForestService.getBamBooForestList(status, pageable));
    }
}
