package com.festival.common.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginReq {

    private String username;
    private String password;

    @Builder
    private MemberLoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
