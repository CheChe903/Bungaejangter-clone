package com.example.demo.controller;


import com.example.demo.domain.dto.request.Product.AddProductRequest;
import com.example.demo.domain.dto.response.Member.RegisterResponse;
import com.example.demo.domain.dto.response.Product.AddProductResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.util.JwtUtil;

import com.example.demo.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/products/*")
public class ProductController extends HttpServlet {

    private ProductService productService;

    private ObjectMapper objectMapper;
    ProductController() {

    }

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.productService = new ProductService(new ProductRepository(), new MemberRepository(), new JwtUtil());
        } catch (IOException e) {
            throw new ServletException("JwtUtil 생성 실패", e);
        }

        this.objectMapper = new ObjectMapper();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        try {
            switch (path) {
                case "/new":
                    addProduct(req, resp);
                    break;
                default:
                    ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
            }
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void addProduct(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        AddProductRequest addProductRequest = objectMapper.readValue(req.getInputStream(), AddProductRequest.class);
        try {
            String authHeader = req.getHeader("Authorization");
            if(authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                AddProductResponse addProductResponse = productService.addProduct(addProductRequest, token);
                ResponseUtil.sendResponse(resp, addProductResponse.getStatus(), addProductResponse);
            }
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Token is not available");
        }
    }

}
