package com.festival.common.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import com.festival.common.security.jwt.JwtTokenProvider;
import com.festival.common.security.jwt.JwtTokenRes;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import com.festival.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();


        String loginType = oAuth2User.getAttribute("loginType");

        // KAKAO_user123@naver.com
        String email = loginType + "_" + oAuth2User.getAttribute("email");
        Optional<Member> findMember = memberRepository.findByEmail(email);

        // 회원이 아닌 경우에 회원 가입 진행
        Member member = null;
        if (findMember.isEmpty()) {
            // KAKAO_user123
            member = Member.builder()
                    .email(email)
                    .loginType(loginType)
                    .memberRole(MemberRole.MEMBER)
                    .build();

            memberRepository.save(member);
        } else {
            member = findMember.orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        }

        // OAuth2User 객체에서 권한 가져옴
        JwtTokenRes jwtToken = jwtTokenProvider.createJwtToken(member.getEmail(), member.getRole().getValue());
/*

        String targetUrl = UriComponentsBuilder.fromUriString("http//localhost:8080/success")
                .queryParam("accessToken", jwtToken.getAccessToken())
                .queryParam("refreshToken", jwtToken.getRefreshToken())
                .queryParam("memberId", String.valueOf(member.getId()))
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
*/

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(200);

        response.getWriter().write(
                objectMapper.writeValueAsString(jwtToken)
        );
    }

}
