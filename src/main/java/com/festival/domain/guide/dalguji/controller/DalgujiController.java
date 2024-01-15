package com.festival.domain.guide.dalguji.controller;

import com.festival.domain.guide.dalguji.dto.DalgujiReq;
import com.festival.domain.guide.dalguji.dto.DalgujiRes;
import com.festival.domain.guide.dalguji.service.DalgujiService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/dalguji")
public class DalgujiController {

    private final DalgujiService dalgujiService;

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @Operation(summary = "달구지 이미지 등록", description = "MULTIPART_FORM_DATA Header")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createDalguji(@Valid @ParameterObject DalgujiReq dalgujiReq) {
        return ResponseEntity.ok().body(dalgujiService.createDalgujiImage(dalgujiReq));
    }

    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @Operation(summary = "달구지 이미지 추가 등록", description = "MULTIPART_FORM_DATA Header")
    @PostMapping(value = "/{college}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addDalguji(@PathVariable("college") @NotNull String college,
                                           @Valid @ParameterObject DalgujiReq dalgujiReq) {
        return ResponseEntity.ok().body(dalgujiService.addDalgugiImage(college, dalgujiReq));
    }

    @Operation(summary = "달구지 이미지 삭제")
    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @DeleteMapping(value = "/{college}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteDalguji(@PathVariable("college") String college,
                                              @RequestParam(name = "imageId", required = false) Integer imageId) {
        if (imageId == null) {
            dalgujiService.deleteDalgujiImagesAll(college);
        } else {
            dalgujiService.deleteDalgujiImage(college, imageId);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "달구지 이미지 조회")
    @PreAuthorize("hasRole('ADMIN') or  hasRole('MANAGER')")
    @GetMapping(value = "/{college}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DalgujiRes> getDalguji(@PathVariable("college") String college) {
        return ResponseEntity.ok().body(dalgujiService.getDalgujiImages(college));
    }
}
