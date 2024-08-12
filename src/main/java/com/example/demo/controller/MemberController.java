package com.example.demo.controller;

import com.example.demo.domain.dto.request.MemberLoginRequest;
import com.example.demo.domain.dto.request.MemberRegisterRequest;
import com.example.demo.service.MemberService;
import com.example.demo.util.ResponseUtil;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.PasswordUtil;
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
        this.memberService = new MemberService(new MemberRepository(), new PasswordUtil());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            switch (path) {
                case "/register":
                    registerMember(req, resp);
                    break;
                case "/login":
                    loginMember(req, resp);
                    break;
                default:
                    ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
            }
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void registerMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MemberRegisterRequest memberRequest = objectMapper.readValue(req.getInputStream(), MemberRegisterRequest.class);
        boolean success = memberService.registerMember(memberRequest);
        if (success) {
            ResponseUtil.sendSuccessResponse(resp, HttpServletResponse.SC_OK,"Registration successful!");
        } else {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Registration failed. Check your input.");
        }
    }

    private void loginMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MemberLoginRequest memberRequest = objectMapper.readValue(req.getInputStream(), MemberLoginRequest.class);
        boolean success = memberService.login(memberRequest);
        if (success) {
            ResponseUtil.sendSuccessResponse(resp, HttpServletResponse.SC_OK, "Login successful!");
        } else {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
        }
    }
}