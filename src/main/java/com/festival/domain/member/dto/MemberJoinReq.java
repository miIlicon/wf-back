package com.festival.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinReq {

    @NotBlank(message = "로그인 아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "회원 권한을 선택해주세요.")
    private String memberRole;

    @Builder
    private MemberJoinReq(String username, String password, String memberRole) {
        this.username = username;
        this.password = password;
        this.memberRole = memberRole;
    }
}
