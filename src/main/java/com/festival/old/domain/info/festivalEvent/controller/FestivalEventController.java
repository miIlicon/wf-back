package com.festival.old.domain.info.festivalEvent.controller;


import com.festival.old.common.base.CommonIdResponse;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventDto;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventListRes;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventReq;
import com.festival.old.domain.info.festivalEvent.data.dto.FestivalEventRes;
import com.festival.old.domain.info.festivalEvent.service.FestivalEventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<CommonIdResponse> createFestivalEvent1(@RequestPart("dto") @Valid FestivalEventReq festivalEventReq, @RequestPart("main-file") @NotEmpty MultipartFile mainFile, @RequestPart("sub-file") List<MultipartFile> subFiles) throws IOException {

        CommonIdResponse commonIdResponse = festivalEventService.create(festivalEventReq, mainFile, subFiles);

        return ResponseEntity.ok().body(commonIdResponse);
    }
    @PostMapping("/festivalEvent/integrated")
    public ResponseEntity<CommonIdResponse> createFestivalEvent2(@Valid FestivalEventDto festivalEventDto) throws IOException {
        CommonIdResponse commonIdResponse = festivalEventService.create(FestivalEventReq.of(festivalEventDto), festivalEventDto.getMainFile(), festivalEventDto.getSubFiles());

        return ResponseEntity.ok().body(commonIdResponse);
    }

    @GetMapping("/festivalEvent/{id}")
    public ResponseEntity<FestivalEventRes> findFestivalEvent(@PathVariable("id") Long festivalEventId){
        return ResponseEntity.ok().body(festivalEventService.find(festivalEventId));
    }
    @GetMapping("/festivalEvent/list")
    public ResponseEntity<Page<FestivalEventListRes>> findFestivalEventList(@RequestParam("page") int offset, @RequestParam("state") boolean state){
        return ResponseEntity.ok().body(festivalEventService.list(offset, state));
    }
    @PutMapping("/festivalEvent/{id}")
    public ResponseEntity<CommonIdResponse> modifyFestivalEvent(@PathVariable("id") Long festivalEventId, @RequestPart("dto") @Valid FestivalEventReq festivalEventReq, @RequestPart("main-file") @NotEmpty MultipartFile mainFile, @RequestPart("sub-file") List<MultipartFile> subFiles) throws IOException {
        CommonIdResponse commonIdResponse = festivalEventService.modify(festivalEventId, festivalEventReq, mainFile, subFiles);
        return ResponseEntity.ok().body(commonIdResponse);
    }

    @DeleteMapping("/festivalEvent/{id}")
    public ResponseEntity<CommonIdResponse> deleteFestivalEvent(@PathVariable("id") Long festivalEventId){
        CommonIdResponse commonIdResponse = festivalEventService.delete(festivalEventId);
        return ResponseEntity.ok().body(commonIdResponse);
    }
}
