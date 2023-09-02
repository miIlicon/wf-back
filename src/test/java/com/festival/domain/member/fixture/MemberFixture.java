package com.festival.domain.member.fixture;

import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;

public class MemberFixture {

    public static Member ADMIN = Member.builder()
            .username("admin")
            .password("1234")
            .memberRole(MemberRole.ADMIN)
            .build();

    public static Member MANAGER1 = Member.builder()
            .username("manager1")
            .password("1234")
            .memberRole(MemberRole.MANAGER)
            .build();

    public static Member MANAGER2 = Member.builder()
            .username("manager2")
            .password("1234")
            .memberRole(MemberRole.MANAGER)
            .build();

}
