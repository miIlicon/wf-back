package com.festival.domain.member.service;

import com.festival.common.exception.custom_exception.DuplicationException;
import com.festival.common.exception.custom_exception.ForbiddenException;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.redis.RedisService;
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

import static com.festival.common.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public Long join(MemberJoinReq memberJoinReq) {
        if (isExistsId(memberJoinReq.getUsername())) {
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

    private boolean isExistsId(String email) {
        return memberRepository.existsByUsername(email);
    }
    private Authentication attemptAuthenticate(MemberLoginReq loginReq) {
        return authenticationManagerBuilder.getObject().authenticate(createAuthenticationToken(loginReq));
    }

    private static UsernamePasswordAuthenticationToken createAuthenticationToken(MemberLoginReq loginReq) {
        return new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword());
    }

    /**
     * @Description
     *  1. 요청으로 들어온 RT를 검증
     *  2. 현재 RT와 요청으로 들어온 RT비교
     *  3. 다르다면 토큰 탈취로 간주 후 로그아웃 처리 + 예외 처리
     */
    public JwtTokenRes rotateToken(String requestRefreshToken) {
        jwtTokenProvider.validateRefreshToken(requestRefreshToken);
        Authentication authentication = jwtTokenProvider.getAuthenticationByRefreshToken(requestRefreshToken);

        String currentRefreshToken = redisService.getRefreshToken(authentication.getName());

        if(!currentRefreshToken.equals(requestRefreshToken)) {
            logout(authentication.getName());
            throw new ForbiddenException(SNATCH_TOKEN);
        }

        redisService.rotateRefreshToken(authentication.getName(), requestRefreshToken);
        return jwtTokenProvider.createJwtToken(authentication);
    }

    public String logout(String username){
        redisService.deleteRefreshToken(username);
        return "로그아웃 처리 되었습니다.";
    }

    public void checkLogin(String refreshToken) {
        jwtTokenProvider.checkLoginByRefreshToken(refreshToken);
    }
}
