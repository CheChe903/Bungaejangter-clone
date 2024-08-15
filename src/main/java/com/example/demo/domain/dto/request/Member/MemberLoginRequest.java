package com.example.demo.domain.dto.request.Member;

public class MemberLoginRequest {
    private String email;
    private String password;

    public MemberLoginRequest() {}

    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
