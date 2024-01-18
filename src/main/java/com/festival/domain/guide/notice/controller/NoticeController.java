package com.festival.domain.guide.notice.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.guide.notice.dto.NoticeListReq;
import com.festival.domain.guide.notice.dto.NoticePageRes;
import com.festival.domain.guide.notice.dto.NoticeReq;
import com.festival.domain.guide.notice.dto.NoticeRes;
import com.festival.domain.guide.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/guide")
public class NoticeController {

    private final NoticeService noticeService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasRole({'ADMIN'})")
    @Operation(summary = "안내사항 등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createGuide(@Valid NoticeReq noticeReq) {
        validationUtils.isGuideValid(noticeReq);
        return ResponseEntity.ok().body(noticeService.createGuide(noticeReq));
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @Operation(summary = "안내사항 수정")
    @PutMapping(value = "/{guideId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateGuide(@PathVariable("guideId") Long guideId, @Valid NoticeReq noticeReq) {
        validationUtils.isGuideValid(noticeReq);
        return ResponseEntity.ok().body(noticeService.updateGuide(guideId, noticeReq));
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @Operation(summary = "안내사항 삭제")
    @DeleteMapping(value = "/{guideId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGuide(@PathVariable("guideId") Long guideId) {
        noticeService.deleteGuide(guideId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "안내사항 조회")
    @GetMapping(value = "/{guideId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticeRes> getGuide(@PathVariable("guideId") Long guideId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(noticeService.getGuide(guideId, httpServletRequest.getRemoteAddr()));
    }

    @PreAuthorize("permitAll()")
    @Operation(summary = "안내사항 목록 조회!")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NoticePageRes> getGuideList(@Valid NoticeListReq noticeListReq) {
        return ResponseEntity.ok().body(noticeService.getGuideList(noticeListReq));
    }

}
