package com.festival.domain.member.service;

import com.festival.common.exception.custom_exception.DuplicationException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.security.JwtTokenProvider;
import com.festival.common.security.dto.JwtTokenRes;
import com.festival.common.security.dto.MemberLoginReq;
import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import com.festival.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.festival.common.exception.ErrorCode.DUPLICATION_ID;
import static com.festival.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public Long join(MemberJoinReq memberJoinReq) {
        if (isLoginIdSave(memberJoinReq.getUsername())) {
            throw new DuplicationException(DUPLICATION_ID);
        }
        Member member = Member.of(memberJoinReq, passwordEncoder.encode(memberJoinReq.getPassword()));
        return memberRepository.save(member).getId();
    }

    public JwtTokenRes login(MemberLoginReq loginReq) {
        Authentication authenticate = attemptAuthenticate(loginReq);
        return jwtTokenProvider.createJwtToken(authenticate);
    }

    public Member getAuthenticationMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER));
    }

    private boolean isLoginIdSave(String email) {
        return memberRepository.existsByUsername(email);
    }
    private Authentication attemptAuthenticate(MemberLoginReq loginReq) {
        return authenticationManagerBuilder.getObject().authenticate(createAuthenticationToken(loginReq));
    }

    private static UsernamePasswordAuthenticationToken createAuthenticationToken(MemberLoginReq loginReq) {
        return new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword());
    }

    public JwtTokenRes rotateToken(String refreshToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        return jwtTokenProvider.createJwtToken(authentication);
    }
}
