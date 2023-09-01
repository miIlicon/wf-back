package com.festival.domain.member.fixture;

import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;

public class MemberFixture {

    public static Member member1 = Member.builder()
            .username("user1")
            .password("1234")
            .memberRole(MemberRole.ADMIN)
            .build();

    public static Member member2 = Member.builder()
            .username("user2")
            .password("1234")
            .memberRole(MemberRole.ADMIN)
            .build();

}
