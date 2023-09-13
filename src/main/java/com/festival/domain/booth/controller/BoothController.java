package com.festival.domain.booth.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.booth.controller.dto.*;
import com.festival.domain.booth.service.BoothService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/booth")
public class BoothController {

    private final BoothService boothService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasAuthority({'ADMIN', 'MANAGER'})")
    @PostMapping
    public ResponseEntity<Long> createBooth(@Valid BoothReq boothReq) {
        validationUtils.isBoothValid(boothReq);
        return ResponseEntity.ok().body(boothService.createBooth(boothReq));
    }

    @PreAuthorize("hasAuthority({'ADMIN', 'MANAGER'})")
    @PutMapping("/{boothId}")
    public ResponseEntity<Long> updateBooth(@Valid BoothReq boothReq, @PathVariable("boothId") Long id) {
        validationUtils.isBoothValid(boothReq);
        return ResponseEntity.ok().body(boothService.updateBooth(boothReq, id));
    }

    @PreAuthorize("hasAuthority({'ADMIN', 'MANAGER'})")
    @DeleteMapping("/{boothId}")
    public ResponseEntity<Void> deleteBooth(@PathVariable("boothId") Long id) {
        boothService.deleteBooth(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{boothId}")
    public ResponseEntity<BoothRes> getBooth(@PathVariable("boothId") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(boothService.getBooth(id,httpServletRequest.getRemoteAddr()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/query/{boothId}")
    public ResponseEntity<BoothRes> getBoothQuery(@PathVariable("boothId") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(boothService.getBoothQuery(id,httpServletRequest.getRemoteAddr()));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public ResponseEntity<BoothPageRes> getBoothList(@Valid BoothListReq boothListReq, Pageable pageable) {
        return ResponseEntity.ok().body(boothService.getBoothList(boothListReq, pageable));
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/search")
    public ResponseEntity<List<BoothSearchRes>> searchBoothList(@RequestParam(name = "keyword") String keyword){
        return ResponseEntity.ok().body(boothService.searchBoothList(keyword));
    }
}
