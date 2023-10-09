package com.festival.domain.member.controller;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.redis.RedisService;
import com.festival.common.security.JwtTokenProvider;
import com.festival.common.security.dto.JwtTokenRes;
import com.festival.common.security.dto.MemberLoginReq;
import com.festival.common.util.JwtTokenUtils;
import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/member")
public class MemberController {

    private final MemberService memberService;
/*

    @PreAuthorize("isAnonymous()")
    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<Void> joinMember(@Valid @ParameterObject MemberJoinReq memberJoinReq) {
        memberService.join(memberJoinReq);
        return ResponseEntity.ok().build();
    }
*/

    @PreAuthorize("isAnonymous()")
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<JwtTokenRes> loginMember(@Valid MemberLoginReq loginReq) {
        return ResponseEntity.ok().body(memberService.login(loginReq));
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logoutMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(memberService.logout(username));
    }

    @GetMapping("/rotate")
    public JwtTokenRes rotateToken(HttpServletRequest request){
        log.info("rotateContoller");
        String refreshToken = JwtTokenUtils.extractBearerToken(request.getHeader("refreshToken"));

        if(refreshToken.isBlank())
            throw new BadRequestException(ErrorCode.EMPTY_REFRESH_TOKEN);

        memberService.checkLogin(refreshToken);

        return memberService.rotateToken(refreshToken);
    }


}
