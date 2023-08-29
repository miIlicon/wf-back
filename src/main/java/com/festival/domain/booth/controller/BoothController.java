package com.festival.domain.booth.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.booth.controller.dto.BoothListReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.booth.controller.dto.BoothRes;
import com.festival.domain.booth.service.BoothService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/booth")
public class BoothController {

    private final BoothService boothService;
    private final ValidationUtils validationUtils;

    @PostMapping
    public ResponseEntity<Long> create(@Valid BoothReq boothReq) throws Exception {
        if (!validationUtils.isBoothValid(boothReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(boothService.createBooth(boothReq));
    }

    @PutMapping("/{boothId}")
    public ResponseEntity<Long> update(@Valid BoothReq boothReq, @PathVariable("boothId") Long id) throws Exception {
        if (!validationUtils.isBoothValid(boothReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(boothService.updateBooth(boothReq, id));
    }

    @DeleteMapping("/{boothId}")
    public ResponseEntity<Void> delete(@PathVariable("boothId") Long id) {
        boothService.deleteBooth(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boothId}")
    public ResponseEntity<BoothRes> get(@PathVariable("boothId") Long id) {
        return ResponseEntity.ok().body(boothService.getBooth(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoothRes>> list(@Valid BoothListReq boothListReq, Pageable pageable) {
        return ResponseEntity.ok().body(boothService.getBoothList(boothListReq, pageable));
    }
}
