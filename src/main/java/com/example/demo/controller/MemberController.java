package com.example.demo.controller;

import com.example.demo.domain.dto.request.MemberLoginRequest;
import com.example.demo.domain.dto.request.MemberRegisterRequest;
import com.example.demo.domain.dto.response.LoginResponse;
import com.example.demo.domain.dto.response.RegisterResponse;
import com.example.demo.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/register", "/login"})
public class MemberController extends HttpServlet {
    private final MemberService memberService;
    private final ObjectMapper objectMapper;

    // 기본 생성자 추가
    public MemberController() {
        this.memberService = new MemberService();
        this.objectMapper = new ObjectMapper();
    }

    public MemberController(MemberService memberService, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/register".equals(path)) {
            registerMember(req, resp);
        } else if ("/login".equals(path)) {
            loginMember(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        }
    }

    private void registerMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MemberRegisterRequest memberRequest = objectMapper.readValue(req.getInputStream(), MemberRegisterRequest.class);

        if (memberRequest.getUsername() == null || memberRequest.getEmail() == null || memberRequest.getPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new RegisterResponse("Missing parameters", HttpServletResponse.SC_BAD_REQUEST));
            return;
        }

        memberService.registerMember(memberRequest.getUsername(), memberRequest.getEmail(), memberRequest.getPassword());

        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), new RegisterResponse("Registration successful!", HttpServletResponse.SC_OK));
    }

    private void loginMember(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MemberLoginRequest memberRequest = objectMapper.readValue(req.getInputStream(), MemberLoginRequest.class);

        if (memberRequest.getEmail() == null || memberRequest.getPassword() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new LoginResponse("Missing parameters", HttpServletResponse.SC_BAD_REQUEST));
            return;
        }

        var member = memberService.login(memberRequest.getEmail(), memberRequest.getPassword());
        if (member != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), new LoginResponse("Login successful! Welcome " + member.getUsername(), HttpServletResponse.SC_OK));
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            objectMapper.writeValue(resp.getWriter(), new LoginResponse("Invalid email or password", HttpServletResponse.SC_UNAUTHORIZED));
        }
    }
}
