package com.festival.common.security.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginReq {

    private String username;
    private String password;

    public MemberLoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
