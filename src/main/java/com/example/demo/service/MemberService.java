package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.dto.request.MemberLoginRequest;
import com.example.demo.domain.dto.request.MemberRegisterRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.PasswordUtil;

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;

    public MemberService(MemberRepository memberRepository, PasswordUtil passwordUtil) {
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
    }

    public boolean registerMember(MemberRegisterRequest request) {
        if (isInvalidRegisterRequest(request)) {
            return false;
        }

        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setEmail(request.getEmail());
        String hashedPassword = passwordUtil.hashPassword(request.getPassword());
        member.setPassword(hashedPassword);

        memberRepository.addMember(member);
        return true;
    }

    public boolean login(MemberLoginRequest request) {
        if (isInvalidLoginRequest(request)) {
            return false;
        }

        Member member = memberRepository.getMemberByEmail(request.getEmail());
        if (member != null) {
            String hashedPassword = passwordUtil.hashPassword(request.getPassword());
            if (member.getPassword().equals(hashedPassword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidRegisterRequest(MemberRegisterRequest request) {
        return request.getUsername() == null || request.getEmail() == null || request.getPassword() == null;
    }

    private boolean isInvalidLoginRequest(MemberLoginRequest request) {
        return request.getEmail() == null || request.getPassword() == null;
    }
}