package com.festival.domain.admin.data.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginReq {

    @NotBlank(message = "id값을 입력해주세요.")
    private String username;

    @NotBlank(message = "password값을 입력해주세요.")
    private String password;

    @Builder
    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
