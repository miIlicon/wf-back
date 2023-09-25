package com.festival.domain.booth.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.booth.controller.dto.*;
import com.festival.domain.booth.service.BoothService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/booth")
public class BoothController {

    private final BoothService boothService;
    private final ValidationUtils validationUtils;

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createBooth(@Valid BoothReq boothReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validationUtils.isBoothValid(boothReq);
        return ResponseEntity.ok().body(boothService.createBooth(boothReq));
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping(value = "/{boothId}", produces = APPLICATION_JSON_VALUE, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> updateBooth(@Valid BoothReq boothReq, @PathVariable("boothId") Long id) {
        validationUtils.isBoothValid(boothReq);
        return ResponseEntity.ok().body(boothService.updateBooth(boothReq, id));
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping(value = "/{boothId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateBoothOperateStatus(@NotNull @RequestParam("operateStatus") String operateStatus, @PathVariable("boothId") Long id) {
        return ResponseEntity.ok().body((Long) boothService.updateBoothOperateStatus(operateStatus, id));
    }


    //@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @DeleteMapping(value = "/{boothId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBooth(@PathVariable("boothId") Long id) {
        boothService.deleteBooth(id);
        return ResponseEntity.ok().build();
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(value =  "/{boothId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BoothRes> getBooth(@PathVariable("boothId") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok().body(boothService.getBooth(id,httpServletRequest.getRemoteAddr()));
    }


    //@PreAuthorize("permitAll()")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<BoothPageRes> getBoothList(@Valid BoothListReq boothListReq) {
        return ResponseEntity.ok().body(boothService.getBoothList(boothListReq));
    }
    //@PreAuthorize("permitAll()")
    @GetMapping(value = "/search", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BoothSearchRes>> searchBoothList(@RequestParam(name = "keyword") String keyword){
        return ResponseEntity.ok().body(boothService.searchBoothList(keyword));
    }
}
