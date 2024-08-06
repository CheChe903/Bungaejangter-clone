package com.example.demo.service;

import com.example.demo.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService();
    }

    @Test
    @DisplayName("회원가입 및 로그인 성공 여부")
    void testRegisterMemberAndLoginSuccess() {
        // Given
        String username = "KIMJINYEONG";
        String email = "cheche903@naver.com";
        String password = "password";

        // When
        memberService.registerMember(username, email, password);
        Member member = memberService.login(email, password);

        // Then
        assertNotNull(member);
        assertEquals(username, member.getUsername());
        assertEquals(email, member.getEmail());
    }

    @Test
    @DisplayName("로그인 실패 (비밀번호) 오류")
    void testLoginFailWrongPassword() {
        // Given
        String username = "KIMJINYEONG";
        String email = "cheche903@naver.com";
        String password = "password";
        String wrongPassword = "wrongPassword";

        // When
        memberService.registerMember(username, email, password);
        Member member = memberService.login(email, wrongPassword);

        // Then
        assertNull(member);
    }

    @Test
    @DisplayName("로그인 실패 (존재하는 회원) 오류")
    void testLoginFailNonexistentEmail() {
        // Given
        String email = "cheche903@naver.com";
        String password = "password";

        // When
        Member member = memberService.login(email, password);

        // Then
        assertNull(member);
    }

    @Test
    @DisplayName("memberId 자동증가 테스트")
    void testIncreaseMeberId() {
        // Given
        String username1 = "KIMJINYEONG";
        String email1 = "cheche903@naver.com";
        String password1 = "password";

        String username2 = "KIMJINYEONG2";
        String email2 = "cheche904@naver.com";
        String password2 = "password";

        // When
        memberService.registerMember(username1, email1, password1);
        memberService.registerMember(username2, email2, password2);

        Member member1 = memberService.login(email1, password1);
        Member member2 = memberService.login(email2, password2);
        // Then
        assertEquals(member1.getMemberId(),1);
        assertEquals(member2.getMemberId(),2);
    }
}
