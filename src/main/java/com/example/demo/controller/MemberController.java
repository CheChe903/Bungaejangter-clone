package com.example.demo.controller;

import com.example.demo.config.JacksonConfig;
import com.example.demo.domain.Member;
import com.example.demo.domain.ProductStatus;
import com.example.demo.domain.dto.request.Member.MemberLoginRequest;
import com.example.demo.domain.dto.request.Member.MemberRegisterRequest;
import com.example.demo.domain.dto.response.Member.MemberDTO;
import com.example.demo.domain.dto.response.Product.ProductDTO;
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
import java.util.List;

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
            this.memberService = new MemberService(MemberRepository.getInstance(), new PasswordUtil(), new JwtUtil());
        } catch (IOException e) {
            throw new ServletException("JwtUtil 생성 실패", e);
        }
        this.objectMapper = JacksonConfig.createObjectMapper();
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        ApiResponse<?> apiResponse;

        try {
            if (path.matches("/\\d+")) {
                Long memberId = extractMemberIdFromPath(path);
                apiResponse = getMemberInfoById(memberId);
            }
            else if(path.matches("/\\d+/products")){
                String statusParam = req.getParameter("status");

                if(statusParam != null && !statusParam.isEmpty()) {
                    try {
                        ProductStatus status = ProductStatus.valueOf(statusParam.toUpperCase());
                        apiResponse =getProductsListSortedStatus(status);
                    } catch (IllegalArgumentException e) {
                        apiResponse = ApiResponseGenerator.fail("Invalid status value", HttpServletResponse.SC_BAD_REQUEST);
                    }
                } else {
                    apiResponse = ApiResponseGenerator.fail("Missing status parameter", HttpServletResponse.SC_BAD_REQUEST);
                }
            }
            else {
                apiResponse = ApiResponseGenerator.fail("Invalid path", HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            apiResponse = ApiResponseGenerator.fail("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        ResponseUtil.sendResponse(resp, apiResponse);
    }

    private Long extractMemberIdFromPath(String path) {
        String idStr = path.substring(1);
        return Long.parseLong(idStr);
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

    private ApiResponse<?> getMemberInfoById(Long memberId) throws IOException {
        try {
            MemberDTO memberDTO = memberService.getMemberById(memberId);
            return ApiResponseGenerator.success(memberDTO, HttpServletResponse.SC_OK, "Get Member successful", "MEMBER_GET");
        } catch (Exception e) {
            return ApiResponseGenerator.fail("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private ApiResponse<?> getProductsListSortedStatus(ProductStatus status) {
        try {
            List<ProductDTO> productDTOList = memberService.getProductsListSortedStatus(status);

            return ApiResponseGenerator.success(productDTOList, HttpServletResponse.SC_OK, "Get Products successful", "PRODUCTS_GET");
        } catch (Exception e) {
            return ApiResponseGenerator.fail("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}