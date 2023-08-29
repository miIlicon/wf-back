package com.festival.domain.member.service;

import com.festival.domain.member.dto.MemberReq;
import com.festival.domain.member.model.Member;
import com.festival.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(MemberReq memberReq) {
        if (isLoginIdSave(memberReq.getLoginId())) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
        Member member = Member.of(memberReq, passwordEncoder.encode(memberReq.getPassword()));
        Member savedMember = memberRepository.save(member);
    }

    private boolean isLoginIdSave(String email) {
        return memberRepository.existsByLoginId(email);
    }
}
