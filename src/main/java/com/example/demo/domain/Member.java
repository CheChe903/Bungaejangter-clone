package com.example.demo.domain;

import com.example.demo.util.PasswordUtil;

import java.math.BigDecimal;
import java.util.Date;

public class Member {
    private Long memberId;
    private String username;
    private String email;
    private String hashedPassword;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) {
        this.hashedPassword = PasswordUtil.hashPassword(password);
    }
}
