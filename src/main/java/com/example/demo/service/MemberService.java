package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.dto.request.Member.MemberLoginRequest;
import com.example.demo.domain.dto.request.Member.MemberRegisterRequest;
import com.example.demo.domain.dto.response.LoginResponse;
import com.example.demo.domain.dto.response.RegisterResponse;
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
        this.jwtUtil =jwtUtil;
    }

    public RegisterResponse registerMember(MemberRegisterRequest registerRequest) {
        if (memberRepository.getMemberByEmail(registerRequest.getEmail()) != null) {
            return new RegisterResponse("Email already registered", HttpServletResponse.SC_BAD_REQUEST, false);
        }

        String hashedPassword = passwordUtil.hashPassword(registerRequest.getPassword());
        Member newMember = new Member(registerRequest.getUsername(), registerRequest.getEmail(), hashedPassword);
        memberRepository.addMember(newMember);

        return new RegisterResponse("Registration successful", HttpServletResponse.SC_OK, true);
    }

    public LoginResponse login(MemberLoginRequest loginRequest) {
        Member member = memberRepository.getMemberByEmail(loginRequest.getEmail());

        if (member == null) {
            return new LoginResponse("Invalid email or password", HttpServletResponse.SC_UNAUTHORIZED, false, null);
        }

        String inputPassword = passwordUtil.hashPassword(loginRequest.getPassword());

        if (Objects.equals(inputPassword, member.getPassword())) {
            String token = jwtUtil.generateToken(member.getMemberId());
            return new LoginResponse("Login successful", HttpServletResponse.SC_OK, true, token);
        } else {
            return new LoginResponse("Invalid email or password", HttpServletResponse.SC_UNAUTHORIZED, false, null);
        }
    }

    private boolean isInvalidRegisterRequest(MemberRegisterRequest request) {
        return request.getUsername() == null || request.getEmail() == null || request.getPassword() == null;
    }

    private boolean isInvalidLoginRequest(MemberLoginRequest request) {
        return request.getEmail() == null || request.getPassword() == null;
    }
}