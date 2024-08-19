package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.dto.request.Member.MemberLoginRequest;
import com.example.demo.domain.dto.request.Member.MemberRegisterRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.PasswordUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Objects;

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    public boolean registerMember(MemberRegisterRequest registerRequest) {
        if (memberRepository.getMemberByEmail(registerRequest.getEmail()) != null) {
            return false;
        }

        String hashedPassword = passwordUtil.hashPassword(registerRequest.getPassword());
        Member newMember = new Member(registerRequest.getUsername(), registerRequest.getEmail(), hashedPassword);
        memberRepository.addMember(newMember);

        return true;
    }

    public String login(MemberLoginRequest loginRequest) {
        Member member = memberRepository.getMemberByEmail(loginRequest.getEmail());

        if (member == null) {
            return null;
        }

        String inputPassword = passwordUtil.hashPassword(loginRequest.getPassword());

        if (Objects.equals(inputPassword, member.getPassword())) {
            return jwtUtil.generateToken(member.getMemberId());
        } else {
            return null;
        }
    }
}
