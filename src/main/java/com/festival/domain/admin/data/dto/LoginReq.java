package com.festival.domain.admin.data.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReq {

    @NotNull(message = "Id값을 입력해주세요")
    String username;

    @NotNull(message = "Password값을 입력해주세요")
    String password;

    @Builder
    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
