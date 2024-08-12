package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.dto.request.MemberLoginRequest;
import com.example.demo.domain.dto.request.MemberRegisterRequest;
import com.example.demo.repository.MemberRepository;
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

        when(passwordUtil.hashPassword(password)).thenReturn(hashedPassword);

        MemberRegisterRequest registerRequest = new MemberRegisterRequest(username, email, password);
        when(memberRepository.getMemberByEmail(email)).thenReturn(null);
        doNothing().when(memberRepository).addMember(any(Member.class));

        // When
        boolean registerSuccess = memberService.registerMember(registerRequest);

        MemberLoginRequest loginRequest = new MemberLoginRequest(email, password);
        when(memberRepository.getMemberByEmail(email)).thenReturn(new Member(username, email, hashedPassword));

        boolean loginSuccess = memberService.login(loginRequest);

        // Then
        assertTrue(registerSuccess);
        assertTrue(loginSuccess);
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

        when(passwordUtil.hashPassword(password)).thenReturn(hashedPassword);
        when(passwordUtil.hashPassword(wrongPassword)).thenReturn("wrongHashedPassword");

        MemberRegisterRequest registerRequest = new MemberRegisterRequest(username, email, password);
        when(memberRepository.getMemberByEmail(email)).thenReturn(new Member(username, email, hashedPassword));

        // When
        memberService.registerMember(registerRequest);
        boolean loginSuccess = memberService.login(new MemberLoginRequest(email, wrongPassword));

        // Then
        assertFalse(loginSuccess);
    }

    @Test
    @DisplayName("로그인 실패 (존재하지 않는 회원) 오류")
    void testLoginFailNonexistentEmail() {
        // Given
        String email = "cheche903@naver.com";
        String password = "password";

        // When
        boolean loginSuccess = memberService.login(new MemberLoginRequest(email, password));

        // Then
        assertFalse(loginSuccess);
    }

}
