package com.festival.domain.admin.controller;

import com.festival.domain.admin.data.dto.JwtTokenRes;
import com.festival.domain.admin.data.dto.LoginReq;
import com.festival.domain.admin.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/admin")
    public JwtTokenRes login(@RequestBody @Valid LoginReq loginReq){
        log.info("login = {}, {}", loginReq.getUsername(), loginReq.getPassword());
        return adminService.login(loginReq);
    }
}
