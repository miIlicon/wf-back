package com.festival.domain.member.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberJoinReq {

    @NotBlank(message = "로그인 아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "회원 권한을 선택해주세요.")
    @Parameter(name = "type" , description = "ADMIN, MANAGER")
    private String memberRole;

    @Builder
    private MemberJoinReq(String username, String password, String memberRole) {
        this.username = username;
        this.password = password;
        this.memberRole = memberRole;
    }
}
