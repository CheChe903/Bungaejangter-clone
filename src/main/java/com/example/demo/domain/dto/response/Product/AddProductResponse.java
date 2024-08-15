package com.example.demo.domain.dto.response.Product;

import com.example.demo.domain.dto.response.ApiResponse;

public class AddProductResponse extends ApiResponse {

    public AddProductResponse(String message, int status, boolean success) {
        super(message, status, success);
    }
}
