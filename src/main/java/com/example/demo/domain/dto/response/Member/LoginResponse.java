package com.example.demo.domain.dto.response.Member;

import com.example.demo.domain.dto.response.ApiResponse;

public class LoginResponse extends ApiResponse {

    private String token;

    public LoginResponse(String message, int status, boolean success, String token) {
        super(message, status, success);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
