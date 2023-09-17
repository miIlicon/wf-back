package com.festival.domain.member.controller;

import com.festival.common.security.dto.JwtTokenRes;
import com.festival.common.security.dto.MemberLoginReq;
import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/member", produces = "application/json", consumes = "multipart/form-data")
public class MemberController {

    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public ResponseEntity<Void> join(@Valid MemberJoinReq memberJoinReq) {
        memberService.join(memberJoinReq);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    public ResponseEntity<JwtTokenRes> login(@Valid MemberLoginReq loginReq) {
        return ResponseEntity.ok().body(memberService.login(loginReq));
    }
}
