package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.dto.request.Member.MemberLoginRequest;
import com.example.demo.domain.dto.request.Member.MemberRegisterRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private PasswordUtil passwordUtil;
    private JwtUtil jwtUtil;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        passwordUtil = mock(PasswordUtil.class);
        jwtUtil = mock(JwtUtil.class);
        memberService = new MemberService(memberRepository, passwordUtil, jwtUtil);
    }

    @Test
    void testRegisterMember_Success() {
        // Arrange
        MemberRegisterRequest registerRequest = new MemberRegisterRequest("username", "email@example.com", "password");
        when(memberRepository.getMemberByEmail(registerRequest.getEmail())).thenReturn(null);
        when(passwordUtil.hashPassword(registerRequest.getPassword())).thenReturn("hashedPassword");

        // Act
        boolean result = memberService.registerMember(registerRequest);

        // Assert
        assertTrue(result);
        verify(memberRepository, times(1)).addMember(any(Member.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        MemberLoginRequest loginRequest = new MemberLoginRequest("email@example.com", "password");
        Member member = new Member("username", "email@example.com", "hashedPassword");
        when(memberRepository.getMemberByEmail(loginRequest.getEmail())).thenReturn(member);
        when(passwordUtil.hashPassword(loginRequest.getPassword())).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(member.getMemberId())).thenReturn("jwtToken");

        // Act
        String token = memberService.login(loginRequest);

        // Assert
        assertNotNull(token);
        assertEquals("jwtToken", token);
    }
}