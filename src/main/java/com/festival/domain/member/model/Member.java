package com.festival.domain.member.model;

import com.festival.domain.member.dto.MemberReq;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Builder
    private Member(String loginId, String password, MemberRole memberRole) {
        this.loginId = loginId;
        this.password = password;
        this.memberRole = memberRole;
    }

    public static Member of(MemberReq memberReq, String password) {
        return Member.builder()
                .loginId(memberReq.getLoginId())
                .password(password)
                .memberRole(settingMemberRole(memberReq.getMemberRole()))
                .build();
    }

    public void update(MemberReq memberReq) {
        this.loginId = memberReq.getLoginId();
        this.password = memberReq.getPassword();
        this.memberRole = settingMemberRole(memberReq.getMemberRole());
    }

    private static MemberRole settingMemberRole(String memberRole) {
        return MemberRole.checkRole(memberRole);
    }
}
