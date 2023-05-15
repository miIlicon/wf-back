package com.festival.domain.admin.controller;

import com.festival.domain.admin.data.dto.JwtTokenRes;
import com.festival.domain.admin.data.dto.LoginReq;
import com.festival.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/admin")
    public JwtTokenRes login(@RequestParam("username") String username, @RequestParam("password") String password){
        LoginReq build = LoginReq.builder()
                .username(username)
                .password(password)
                .build();
        return adminService.login(build);
    }
}
