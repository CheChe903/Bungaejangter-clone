package com.example.demo.domain.dto.response;

public class RegisterResponse extends ApiResponse {
    public RegisterResponse(String message, int status, boolean success) {
        super(message, status, success);
    }
}