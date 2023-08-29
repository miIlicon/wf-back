package com.festival.domain.member.controller;

import com.festival.domain.member.dto.MemberReq;
import com.festival.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid MemberReq memberReq) {
        memberService.join(memberReq);
        return ResponseEntity.ok().build();
    }
}
