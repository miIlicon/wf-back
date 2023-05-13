package com.festival.domain.admin.data.dto;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class JwtTokenRes {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public JwtTokenRes(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}