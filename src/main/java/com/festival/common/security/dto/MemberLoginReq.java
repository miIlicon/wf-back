package com.festival.common.security.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginReq {

    private String loginId;
    private String password;

    public MemberLoginReq(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
