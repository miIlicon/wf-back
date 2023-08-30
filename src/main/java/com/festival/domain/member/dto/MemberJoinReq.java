package com.festival.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberJoinReq {

    @NotBlank(message = "로그인 아이디를 입력 해주세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력 해주세요.")
    private String password;

    @NotBlank(message = "회원 권한을 선택 해주세요.")
    private String memberRole;

    @Builder
    private MemberJoinReq(String loginId, String password, String memberRole) {
        this.loginId = loginId;
        this.password = password;
        this.memberRole = memberRole;
    }

    public static MemberJoinReq of(String loginId, String password, String memberRole) {
        return MemberJoinReq.builder()
                .loginId(loginId)
                .password(password)
                .memberRole(memberRole)
                .build();
    }
}