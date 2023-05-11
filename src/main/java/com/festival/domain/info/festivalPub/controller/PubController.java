package com.festival.domain.info.festivalPub.controller;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import com.festival.domain.info.festivalPub.service.PubService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pubs")
public class PubController {

    private final PubService pubService;

    @PostMapping("/new")
    public ResponseEntity<PubResponse> createPub(@RequestPart("dto") @Valid PubRequest dto,
                                                 @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(pubService.create(1L, dto, file, files));
    }

    @PutMapping("/modify")
    public ResponseEntity<PubResponse> modifyPub(@RequestParam("id") Long pubId, @RequestPart("dto") @Valid PubRequest dto,
                                                 @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        return ResponseEntity.ok().body(pubService.modify(1L, pubId, dto, file, files));
    }

    @DeleteMapping("/pub")
    public ResponseEntity<PubResponse> deletePub(@RequestParam("id") Long pubId) {
        return ResponseEntity.ok().body(pubService.delete(1L, pubId));
    }

    @GetMapping("/pub")
    public ResponseEntity<PubResponse> getPub(@RequestParam("id") Long pubId) {
        return ResponseEntity.ok().body(pubService.getPub(1L, pubId));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<PubResponse>> getPubs(@RequestParam("page") int offset) {
        return ResponseEntity.ok().body(pubService.getPubs(1L, offset));
    }

    @GetMapping("/list/state")
    public ResponseEntity<Page<PubResponse>> getPubsForState(@RequestParam("page") int offset, @RequestParam("state") Boolean state) {
        return ResponseEntity.ok().body(pubService.getPubsForState(1L, offset, state));
    }
}