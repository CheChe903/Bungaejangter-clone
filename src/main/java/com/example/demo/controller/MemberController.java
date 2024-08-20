package com.example.demo.controller;

import com.example.demo.domain.dto.request.Member.MemberLoginRequest;
import com.example.demo.domain.dto.request.Member.MemberRegisterRequest;
import com.example.demo.support.ApiResponse;
import com.example.demo.support.ApiResponseGenerator;
import com.example.demo.domain.dto.response.Member.LoginResponse;
import com.example.demo.service.MemberService;
import com.example.demo.util.JwtUtil;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.PasswordUtil;
import com.example.demo.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/member/*")
public class MemberController extends HttpServlet {
    private MemberService memberService;
    private ObjectMapper objectMapper;

    public MemberController() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.memberService = new MemberService(new MemberRepository(), new PasswordUtil(), new JwtUtil());
        } catch (IOException e) {
            throw new ServletException("JwtUtil 생성 실패", e);
        }
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        ApiResponse<?> apiResponse;

        try {
            switch (path) {
                case "/register":
                    apiResponse = registerMember(req);
                    break;
                case "/login":
                    apiResponse = loginMember(req);
                    break;
                default:
                    apiResponse = ApiResponseGenerator.fail("Invalid path", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            apiResponse = ApiResponseGenerator.fail("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        ResponseUtil.sendResponse(resp, apiResponse);
    }

    private ApiResponse<?> registerMember(HttpServletRequest req) throws IOException {
        MemberRegisterRequest memberRequest = objectMapper.readValue(req.getInputStream(), MemberRegisterRequest.class);
        boolean success = memberService.registerMember(memberRequest);
        if (success) {
            return ApiResponseGenerator.success(HttpServletResponse.SC_OK, "Registration successful", "REG_SUCCESS");
        } else {
            return ApiResponseGenerator.fail("Email already registered", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private ApiResponse<?> loginMember(HttpServletRequest req) throws IOException {
        MemberLoginRequest memberRequest = objectMapper.readValue(req.getInputStream(), MemberLoginRequest.class);
        String token = memberService.login(memberRequest);
        if (token != null) {
            LoginResponse loginResponse = new LoginResponse(token);
            return ApiResponseGenerator.success(loginResponse, HttpServletResponse.SC_OK, "Login successful", "LOGIN_SUCCESS");
        } else {
            return ApiResponseGenerator.fail("AUTH001", "Invalid email or password", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}