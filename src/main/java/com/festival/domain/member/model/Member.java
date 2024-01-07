package com.festival.domain.member.model;

import com.festival.common.security.oauth.SocialCode;
import com.festival.domain.member.dto.MemberJoinReq;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends AuthDetailsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    private Member(String email, SocialCode socialCode, String username, String password, MemberRole memberRole) {
        this.email = email;
        this.socialCode = socialCode;
        this.role = MemberRole.MEMBER;
        this.username = username;
        this.password = password;
        this.memberRoles.add(memberRole);
    }

    public static Member of(MemberJoinReq memberJoinReq, String password) {
        Member member = new Member();
        member.username = memberJoinReq.getUsername();
        member.password = password;
        member.memberRoles.add(settingMemberRole(memberJoinReq.getMemberRole()));
        return member;
    }

    public void update(MemberJoinReq memberJoinReq) {
        this.username = memberJoinReq.getUsername();
        this.password = memberJoinReq.getPassword();
        if(!this.memberRoles.contains(settingMemberRole(memberJoinReq.getMemberRole()))) {
            this.memberRoles.add(settingMemberRole(memberJoinReq.getMemberRole()));
        }
    }

    private static MemberRole settingMemberRole(String memberRole) {
        return MemberRole.checkMemberRole(memberRole);
    }
}
