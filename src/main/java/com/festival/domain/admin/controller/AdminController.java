package com.festival.domain.admin.controller;

import com.festival.domain.admin.data.dto.JwtTokenRes;
import com.festival.domain.admin.data.dto.LoginReq;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final AdminService adminService;


    @PreAuthorize("isAnonymous()")
    @GetMapping("/admin")
    public JwtTokenRes login(@RequestBody LoginReq loginReq){
        JwtTokenRes token = adminService.login(loginReq);
        return token;
    }
}
