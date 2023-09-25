package com.festival.domain.guide.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.guide.dto.GuideListReq;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/guide")
public class GuideController {

    private final GuideService guideService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasRole({'ADMIN'})")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createGuide(@Valid GuideReq guideReq) {
        validationUtils.isGuideValid(guideReq);
        return ResponseEntity.ok().body(guideService.createGuide(guideReq));
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @PutMapping(value = "/{guideId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateGuide(@PathVariable Long guideId, @Valid GuideReq guideReq) {
        validationUtils.isGuideValid(guideReq);
        return ResponseEntity.ok().body(guideService.updateGuide(guideId, guideReq));
    }

    //@PreAuthorize("hasRole({'ADMIN'})")
    @DeleteMapping(value = "/{guideId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGuide(@PathVariable Long guideId) {
        guideService.deleteGuide(guideId);
        return ResponseEntity.ok().build();
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(value = "/{guideId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GuideRes> getGuide(@PathVariable Long guideId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(guideService.getGuide(guideId, httpServletRequest.getRemoteAddr()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<GuidePageRes> getGuideList(@Valid @RequestBody GuideListReq guideListReq) {
        return ResponseEntity.ok().body(guideService.getGuideList(guideListReq));
    }
}
