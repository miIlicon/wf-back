package com.festival.domain.member.controller;

import com.festival.common.security.dto.JwtTokenRes;
import com.festival.common.security.dto.MemberLoginReq;
import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/member")
public class MemberController {

    private final MemberService memberService;

    //@PreAuthorize("isAnonymous()")
    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@Valid @ParameterObject MemberJoinReq memberJoinReq) {
        memberService.join(memberJoinReq);
        return ResponseEntity.ok().build();
    }

    //@PreAuthorize("isAnonymous()")
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<JwtTokenRes> loginMember(@Valid MemberLoginReq loginReq) {
        return ResponseEntity.ok().body(memberService.login(loginReq));
    }
}
