package com.festival.domain.guide.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.guide.dto.GuideRes;
import com.festival.domain.guide.service.GuideService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/guide")
public class GuideController {

    private final GuideService guideService;
    private final ValidationUtils validationUtils;

    @PostMapping
    public ResponseEntity<Long> create(@Valid GuideReq guideReq) throws Exception {
        if (!validationUtils.isGuideValid(guideReq)) {
            throw new Exception();
        }
        Long id = guideService.createGuide(guideReq);
        return ResponseEntity.ok().body(id);
    }

    @PutMapping("/{guideId}")
    public ResponseEntity<Long> update(@PathVariable Long guideId, @Valid GuideReq guideReq) throws Exception {
        if (!validationUtils.isGuideValid(guideReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(guideService.updateGuide(guideId, guideReq));
    }

    @DeleteMapping("/{guideId}")
    public ResponseEntity<Void> delete(@PathVariable Long guideId) {
        guideService.deleteGuide(guideId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{guideId}")
    public ResponseEntity<GuideRes> get(@PathVariable Long guideId) {
        return ResponseEntity.ok().body(guideService.getGuide(guideId));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<GuideRes>> getList(@NotNull(message = "상태값을 입력해주세요") String status, Pageable pageable) {
        return ResponseEntity.ok().body(guideService.getGuideList(status, pageable));
    }

}
