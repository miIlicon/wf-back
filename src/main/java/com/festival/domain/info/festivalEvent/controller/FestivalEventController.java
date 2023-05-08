package com.festival.domain.info.festivalEvent.controller;


import com.festival.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.domain.info.festivalEvent.service.FestivalEventService;
import com.festival.domain.info.festivalPub.data.dto.request.PubRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FestivalEventController {

    private final FestivalEventService festivalEventService;
    @PostMapping("/festivalEvent")
    public void createFestivalEvent(@RequestPart("dto") @Valid FestivalEventReq festivalEventReq, @RequestPart("main-file") @NotEmpty MultipartFile mainFile, @RequestPart("sub-file") List<MultipartFile> subFiles) throws IOException {

        FestivalEventRes festivalEventRes = festivalEventService.create(festivalEventReq, mainFile, subFiles);

    }
}
