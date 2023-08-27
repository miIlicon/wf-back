package com.festival.old.domain.fleaMarket.controller;

import com.festival.old.common.base.CommonIdResponse;
import com.festival.old.domain.fleaMarket.data.dto.request.FleaMarketRequest;
import com.festival.old.domain.fleaMarket.data.dto.response.FleaMarketListResponse;
import com.festival.old.domain.fleaMarket.data.dto.response.FleaMarketResponse;
import com.festival.old.domain.fleaMarket.service.FleaMarketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FleaMarketController {

    private final FleaMarketService fleaMarketService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/flea-market")
    public ResponseEntity<CommonIdResponse> createFleaMarket(@RequestPart("dto") @Valid FleaMarketRequest dto,
                                                             @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(fleaMarketService.create(dto, file, files));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/flea-market/{id}")
    public ResponseEntity<CommonIdResponse> modifyFleaMarket(@PathVariable("id") Long pubId, @RequestPart("dto") @Valid FleaMarketRequest dto,
                                                 @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(fleaMarketService.modify( pubId, dto, file, files));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/flea-market/{id}")
    public ResponseEntity<CommonIdResponse> deleteFleaMarket(@PathVariable("id") Long fleaMarketId) {
        return ResponseEntity.ok().body(fleaMarketService.delete(fleaMarketId));
    }

    @GetMapping("/flea-market/{id}")
    public ResponseEntity<FleaMarketResponse> getFleaMarket(@PathVariable("id") Long fleaMarketId) {
        return ResponseEntity.ok().body(fleaMarketService.getFleaMarket(fleaMarketId));
    }

    @GetMapping("/flea-market/list")
    public ResponseEntity<Page<FleaMarketListResponse>> getFleaMarkets(@RequestParam("page") int offset, @RequestParam("state") boolean state) {
        return ResponseEntity.ok().body(fleaMarketService.getFleaMarkets(offset, state));
    }

}
