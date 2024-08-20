package com.example.demo.util;

import com.example.demo.support.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendResponse(HttpServletResponse resp, ApiResponse<?> apiResponse) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(apiResponse.getStatus());
        objectMapper.writeValue(resp.getOutputStream(), apiResponse);
    }
}