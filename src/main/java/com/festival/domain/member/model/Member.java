package com.festival.domain.member.model;

import com.festival.domain.member.dto.MemberJoinReq;
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

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String loginType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    private Member(String email, String username, String password, MemberRole memberRole, String loginType) {
        this.email = email;
        this.role = memberRole;
        this.username = username;
        this.password = password;
        this.loginType = loginType;
    }

    public static Member of(MemberJoinReq memberJoinReq, String password) {
        Member member = new Member();
        member.username = memberJoinReq.getUsername();
        member.password = password;
        member.role = settingMemberRole(memberJoinReq.getMemberRole());
        return member;
    }

    public void update(MemberJoinReq memberJoinReq) {
        this.username = memberJoinReq.getUsername();
        this.password = memberJoinReq.getPassword();
        if(!this.role.equals(memberJoinReq.getMemberRole())) {
            this.role = settingMemberRole(memberJoinReq.getMemberRole());
        }
    }

    private static MemberRole settingMemberRole(String memberRole) {
        return MemberRole.checkMemberRole(memberRole);
    }
}
