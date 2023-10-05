package com.festival.common.security;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.http.HttpHeaders;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = JwtTokenUtils.extractBearerToken(request.getHeader("accessToken"));

        if(!request.getRequestURI().equals("/api/v2/member/rotate") && accessToken != null) { // 토큰 재발급의 요청이 아니면서 accessToken이 존재할 때
            if (jwtTokenProvider.validateToken(accessToken)) { // 토큰이 유효한 경우
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }




}
