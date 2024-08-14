package com.example.demo.util;

import com.example.demo.domain.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendResponse(HttpServletResponse resp, int statusCode, ApiResponse apiResponse) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), apiResponse);
    }

    public static void sendSuccessResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        sendResponse(resp, statusCode, new ApiResponse(message, statusCode, true));
    }

    public static void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        sendResponse(resp, statusCode, new ApiResponse(message, statusCode, false));
    }
}
