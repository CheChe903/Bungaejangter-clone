package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.PasswordUtil;

public class MemberService {
    private final MemberRepository memberRepository = new MemberRepository();
    private final PasswordUtil passwordUtil = new PasswordUtil();

    // 회원 가입
    public void registerMember(String username, String email, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        String hashedPassword = passwordUtil.hashPassword(password);
        member.setPassword(hashedPassword);

        memberRepository.addMember(member);
    }

    // 로그인
    public Member login(String email, String password) {
        Member member = memberRepository.getMemberByEmail(email);
        if (member != null) {

            String hashedPassword = passwordUtil.hashPassword(password);
            if (member.getPassword().equals(hashedPassword)) {
                return member;
            }
        }
        return null;
    }
}
