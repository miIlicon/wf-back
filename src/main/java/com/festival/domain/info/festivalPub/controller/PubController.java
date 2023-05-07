package com.festival.domain.info.festivalPub.controller;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.service.PubService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pub")
public class PubController {

    private final PubService pubService;

    @PostMapping("/new")
    public ResponseEntity<PubResponse> createPub(@RequestPart("dto") @Valid PubRequest dto,
                                                 @RequestPart("main-file") @NotEmpty MultipartFile file, @RequestPart("sub-file") List<MultipartFile> files) throws IOException {
        log.debug("start pub create");
        dto.setMainFile(file);
        if (!files.isEmpty()) {
            dto.setSubFiles(files);
        }
        return ResponseEntity.ok().body(pubService.create(1L, dto));
    }
}
