package com.example.demo.domain.dto.response;

public class LoginResponse extends ApiResponse {
    public LoginResponse(String message, int status, boolean success) {
        super(message, status, success);
    }
}