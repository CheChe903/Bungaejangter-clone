package com.example.demo.domain.dto.response.Member;

import com.example.demo.domain.dto.response.ApiResponse;

public class RegisterResponse extends ApiResponse {
    public RegisterResponse(String message, int status, boolean success) {
        super(message, status, success);
    }
}