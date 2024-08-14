package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.dto.request.MemberLoginRequest;
import com.example.demo.domain.dto.request.MemberRegisterRequest;
import com.example.demo.domain.dto.response.LoginResponse;
import com.example.demo.domain.dto.response.RegisterResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 및 로그인 성공 여부")
    void testRegisterMemberAndLoginSuccess() {
        // Given
        String username = "KIMJINYEONG";
        String email = "cheche903@naver.com";
        String password = "password";
        String hashedPassword = "hashedPassword";
        String token = jwtUtil.generateToken(1L);

        // Mocking
        when(passwordUtil.hashPassword(password)).thenReturn(hashedPassword);
        when(memberRepository.getMemberByEmail(email)).thenReturn(null);
        doNothing().when(memberRepository).addMember(any(Member.class));
        when(jwtUtil.generateToken(1L)).thenReturn(token);

        MemberRegisterRequest registerRequest = new MemberRegisterRequest(username, email, password);

        // When
        RegisterResponse registerResponse = memberService.registerMember(registerRequest);

        when(memberRepository.getMemberByEmail(email)).thenReturn(new Member(username, email, hashedPassword));

        MemberLoginRequest loginRequest = new MemberLoginRequest(email, password);
        LoginResponse loginResponse = memberService.login(loginRequest);

        // Then
        assertTrue(registerResponse.isSuccess());
        assertEquals("Registration successful", registerResponse.getMessage());

        assertTrue(loginResponse.isSuccess());
        assertEquals("Login successful", loginResponse.getMessage());
        assertEquals(token, loginResponse.getToken());
    }

    @Test
    @DisplayName("로그인 실패 (비밀번호) 오류")
    void testLoginFailWrongPassword() {
        // Given
        String username = "KIMJINYEONG";
        String email = "cheche903@naver.com";
        String password = "password";
        String wrongPassword = "wrongPassword";
        String hashedPassword = "hashedPassword";

        // Mocking
        when(passwordUtil.hashPassword(password)).thenReturn(hashedPassword);
        when(passwordUtil.hashPassword(wrongPassword)).thenReturn("wrongHashedPassword");
        when(memberRepository.getMemberByEmail(email)).thenReturn(new Member(username, email, hashedPassword));

        MemberRegisterRequest registerRequest = new MemberRegisterRequest(username, email, password);

        // When
        memberService.registerMember(registerRequest);

        MemberLoginRequest loginRequest = new MemberLoginRequest(email, wrongPassword);
        LoginResponse loginResponse = memberService.login(loginRequest);

        // Then
        assertFalse(loginResponse.isSuccess());
        assertEquals("Invalid email or password", loginResponse.getMessage());
        assertNull(loginResponse.getToken());
    }

    @Test
    @DisplayName("로그인 실패 (존재하지 않는 회원) 오류")
    void testLoginFailNonexistentEmail() {
        // Given
        String email = "cheche903@naver.com";
        String password = "password";

        // Mocking
        when(memberRepository.getMemberByEmail(email)).thenReturn(null);

        MemberLoginRequest loginRequest = new MemberLoginRequest(email, password);

        // When
        LoginResponse loginResponse = memberService.login(loginRequest);

        // Then
        assertFalse(loginResponse.isSuccess());
        assertEquals("Invalid email or password", loginResponse.getMessage());
        assertNull(loginResponse.getToken());
    }
}
