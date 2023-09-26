package com.festival.domain.booth.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.booth.controller.dto.*;
import com.festival.domain.booth.service.BoothService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/booth")
public class BoothController {

    private final BoothService boothService;
    private final ValidationUtils validationUtils;

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "부스 등록")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createBooth(@Valid @ParameterObject BoothReq boothReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validationUtils.isBoothValid(boothReq);
        return ResponseEntity.ok().body(boothService.createBooth(boothReq));
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "부스 업데이트(전체)")
    @PutMapping(value = "/{boothId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateBooth(@Valid @ParameterObject BoothReq boothReq, @PathVariable("boothId") Long id) {
        validationUtils.isBoothValid(boothReq);
        return ResponseEntity.ok().body(boothService.updateBooth(boothReq, id));
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "부스 운영 상태 업데이트")
    @PutMapping(value = "/{boothId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateBoothOperateStatus(@NotNull @RequestParam("operateStatus") String operateStatus, @PathVariable("boothId") Long id) {
        return ResponseEntity.ok().body((Long) boothService.updateBoothOperateStatus(operateStatus, id));
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "부스 삭제")
    @DeleteMapping(value = "/{boothId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBooth(@PathVariable("boothId") Long id) {
        boothService.deleteBooth(id);
        return ResponseEntity.ok().build();
    }

    //@PreAuthorize("permitAll()")
    @Operation(summary = "부스 단건 조회")
    @GetMapping(value =  "/{boothId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoothRes> getBooth(@PathVariable("boothId") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(boothService.getBooth(id,httpServletRequest.getRemoteAddr()));
    }

    //@PreAuthorize("permitAll()")
    @Operation(summary = "부스 목록 조회")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoothPageRes> getBoothList(@Valid @ParameterObject BoothListReq boothListReq) {
        return ResponseEntity.ok().body(boothService.getBoothList(boothListReq));
    }

    //@PreAuthorize("permitAll()")
    @Operation(summary = "부스 검색")
    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoothSearchRes>> searchBoothList(@RequestParam(name = "keyword") String keyword){
        return ResponseEntity.ok().body(boothService.searchBoothList(keyword));
    }
}
