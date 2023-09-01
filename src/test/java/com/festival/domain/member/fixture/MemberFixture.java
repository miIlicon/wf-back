package com.festival.domain.member.fixture;

import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;

public class MemberFixture {

    public static Member member = Member.builder()
            .username("user")
            .password("1234")
            .memberRole(MemberRole.ADMIN)
            .build();

}
