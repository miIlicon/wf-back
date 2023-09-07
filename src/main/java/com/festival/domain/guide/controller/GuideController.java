package com.festival.domain.guide.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.guide.dto.GuidePageRes;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.service.GuideService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/guide")
public class GuideController {

    private final GuideService guideService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @PostMapping
    public ResponseEntity<Long> createGuide(@Valid GuideReq guideReq) throws Exception {
        if (!validationUtils.isGuideValid(guideReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(guideService.createGuide(guideReq));
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @PutMapping("/{guideId}")
    public ResponseEntity<Long> updateGuide(@PathVariable Long guideId, @Valid GuideReq guideReq) throws Exception {
        if (!validationUtils.isGuideValid(guideReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(guideService.updateGuide(guideId, guideReq));
    }

    @PreAuthorize("hasAuthority({'ADMIN'})")
    @DeleteMapping("/{guideId}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long guideId) {
        guideService.deleteGuide(guideId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{guideId}")
    public ResponseEntity<GuideRes> getGuide(@PathVariable Long guideId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(guideService.getGuide(guideId, httpServletRequest.getRemoteAddr()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public ResponseEntity<GuidePageRes> getGuideList(@NotNull(message = "상태값을 입력해주세요") String status, Pageable pageable) {
        return ResponseEntity.ok().body(guideService.getGuideList(status, pageable));
    }
}
