package com.festival.old.domain.admin.service;


import com.festival.old.domain.admin.data.dto.JwtTokenRes;
import com.festival.old.domain.admin.data.dto.LoginReq;
import com.festival.old.global.security.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public JwtTokenRes login(LoginReq loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword());
        // 인증객체 생성 (authenticated 값은 false)
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();

        // 실제 인증 과정으로 authenticate실행 시 CustomUserDetailsService의 loadByUserName실행
        return tokenProvider.generateToken(authenticate, "ADMIN");
    }
}
