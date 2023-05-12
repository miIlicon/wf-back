package com.festival.domain.fleaMarket.controller;

import com.festival.domain.fleaMarket.data.dto.request.FleaMarketRequest;
import com.festival.domain.fleaMarket.data.dto.response.FleaMarketResponse;
import com.festival.domain.fleaMarket.service.FleaMarketService;
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
@RequestMapping("/api/v1/flea-markets")
public class FleaMarketController {

    private final FleaMarketService fleaMarketService;

    @PostMapping("/flea-market")
    public ResponseEntity<FleaMarketResponse> createFleaMarket(@RequestPart("dto") @Valid FleaMarketRequest dto,
                                                        @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(fleaMarketService.create(1L, dto, file, files));
    }

    @PutMapping("/flea-market")
    public ResponseEntity<FleaMarketResponse> modifyFleaMarket(@RequestParam("id") Long pubId, @RequestPart("dto") @Valid FleaMarketRequest dto,
                                                 @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(fleaMarketService.modify(1L, pubId, dto, file, files));
    }

    @DeleteMapping("/flea-market")
    public ResponseEntity<FleaMarketResponse> deleteFleaMarket(@RequestParam("id") Long fleaMarketId) {
        return ResponseEntity.ok().body(fleaMarketService.delete(1L, fleaMarketId));
    }

    @GetMapping("/flea-market")
    public ResponseEntity<FleaMarketResponse> getFleaMarket(@RequestParam("id") Long fleaMarketId) {
        return ResponseEntity.ok().body(fleaMarketService.getFleaMarket(1L, fleaMarketId));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<FleaMarketResponse>> getFleaMarkets(@RequestParam("page") int offset) {
        return ResponseEntity.ok().body(fleaMarketService.getFleaMarkets(1L, offset));
    }

    @GetMapping("/list/state")
    public ResponseEntity<Page<FleaMarketResponse>> getFleaMarketsForState(@RequestParam("page") int offset, @RequestParam("state") Boolean state) {
        return ResponseEntity.ok().body(fleaMarketService.getFleaMarketsForState(1L, offset, state));
    }
}
