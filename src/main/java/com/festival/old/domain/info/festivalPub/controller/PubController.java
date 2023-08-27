package com.festival.old.domain.info.festivalPub.controller;

import com.festival.old.common.base.CommonIdResponse;
import com.festival.old.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.old.domain.info.festivalPub.data.dto.response.PubListResponse;
import com.festival.old.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.old.domain.info.festivalPub.service.PubService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PubController {

    private final PubService pubService;

    @PostMapping("/pub")
    public ResponseEntity<CommonIdResponse> createPub(@RequestPart("dto") @Valid PubRequest dto,
                                                      @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(pubService.create(dto, file, files));
    }

    @PutMapping("/pub/{id}")
    public ResponseEntity<CommonIdResponse> modifyPub(@PathVariable("id") Long pubId, @RequestPart("dto") @Valid PubRequest dto,
                                                      @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(pubService.modify(pubId, dto, file, files));
    }

    @DeleteMapping("/pub/{id}")
    public ResponseEntity<CommonIdResponse> deletePub(@PathVariable("id") Long pubId) {
        return ResponseEntity.ok().body(pubService.delete(pubId));
    }

    @GetMapping("/pub/{id}")
    public ResponseEntity<PubResponse> getPub(@PathVariable("id") Long pubId) {
        return ResponseEntity.ok().body(pubService.getPub(pubId));
    }

    @GetMapping("/pub/list")
    public ResponseEntity<Page<PubListResponse>> getPubs(@RequestParam("page") int offset, @RequestParam("state") boolean state) {
        return ResponseEntity.ok().body(pubService.getPubs(offset, state));
    }
}
