package com.festival.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.common.exception.CustomException;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.ErrorResponse;
import com.festival.common.infra.Alert.discord.DiscordService;
import com.festival.common.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    //    private final SlackService slackService;
    private final DiscordService discordService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            log.info("jwtFilter" + request.getRequestURI());
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
            filterChain.doFilter(request, response);

        } catch (CustomException e){
            ErrorCode errorCode = e.getErrorCode();

//            slackService.sendSlackAlertLog(e, request);
            discordService.sendDiscordAlertLog(e, request);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(errorCode.getStatus().value());
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods","*");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers",
                    "Origin, X-Requested-With, Content-Type, Accept, accessToken, refreshToken");
            if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else{
                log.error(errorCode.getMessage());
                response.getWriter().write(
                        objectMapper.writeValueAsString(new ErrorResponse(errorCode.getMessage()))
                );
            }
        }
    }
}
