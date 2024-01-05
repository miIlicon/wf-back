package com.festival.common.interceptor;

import com.festival.common.security.JwtTokenProvider;
import com.festival.common.util.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("Authentication Interceptor : " + request.getRequestURI());
            /*
            if(request.getMethod().equals("OPTIONS")){
                filterChain.doFilter(request, response);
            }
            */
        String accessToken = JwtTokenUtils.extractBearerToken(request.getHeader("accessToken"));

        if (!request.getRequestURI().equals("/api/v2/member/rotate") && accessToken != null) { // 토큰 재발급의 요청이 아니면서 accessToken이 존재할 때
            jwtTokenProvider.checkLoginByAccessToken(accessToken);

            if (jwtTokenProvider.validateAccessToken(accessToken)) { // 토큰이 유효한 경우 and 로그인 상태
                Authentication authentication = jwtTokenProvider.getAuthenticationByAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        return true;
    }
}
