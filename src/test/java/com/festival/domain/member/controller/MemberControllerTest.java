package com.festival.domain.member.controller;

import com.festival.common.security.dto.JwtTokenRes;
import com.festival.common.security.dto.MemberLoginReq;
import com.festival.domain.member.dto.MemberJoinReq;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.model.MemberRole;
import com.festival.domain.member.service.MemberService;
import com.festival.domain.util.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTestSupport {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @DisplayName("유저는 회원가입을 할 수 있다.")
    @Test
    void joinMember() throws Exception {
        //given
        MemberJoinReq memberJoinReq = MemberJoinReq.builder()
                .username("test")
                .password("test")
                .memberRole("ADMIN")
                .build();

        //when //then
        mockMvc.perform(post("/api/v2/member/join")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("username", memberJoinReq.getUsername())
                        .param("password", memberJoinReq.getPassword())
                        .param("memberRole", memberJoinReq.getMemberRole()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("이미 가입된 id는 회원가입을 할 수 없다.")
    @Test
    void createMemberWithSameID() throws Exception {
        //given
        Member member = Member.builder()
                .username("test")
                .password("test")
                .memberRole(MemberRole.ADMIN)
                .build();
        memberRepository.saveAndFlush(member);

        MemberJoinReq memberJoinReq = MemberJoinReq.builder()
                .username("test")
                .password("test")
                .memberRole("ADMIN")
                .build();

        //when //then
        MvcResult mvcResult = mockMvc.perform(post("/api/v2/member/join")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("username", memberJoinReq.getUsername())
                        .param("password", memberJoinReq.getPassword())
                        .param("memberRole", memberJoinReq.getMemberRole())
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andReturn();
    }

    @DisplayName("회원가입한 사용자는 로그인을 할 수 있다.")
    @Test
    void loginMember() throws Exception {
        //given
        MemberJoinReq memberJoinReq = MemberJoinReq.builder()
                .username("test")
                .password("test")
                .memberRole("ADMIN")
                .build();
        memberService.join(memberJoinReq);

        MemberLoginReq loginReq = MemberLoginReq.builder()
                .username("test")
                .password("test")
                .build();

        //when
        MvcResult mvcResult = mockMvc.perform(post("/api/v2/member/login")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("username", loginReq.getUsername())
                        .param("password", loginReq.getPassword())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //then
        String result = mvcResult.getResponse().getContentAsString();
        JwtTokenRes jwtTokenRes = objectMapper.readValue(result, JwtTokenRes.class);
        assertThat(jwtTokenRes).isNotNull();
        assertThat(jwtTokenRes.getAccessToken()).isNotNull();
        assertThat(jwtTokenRes.getRefreshToken()).isNotNull();
        assertThat(jwtTokenRes.getTokenType()).isEqualTo("Bearer");
    }
}