package com.example.demo.controller;


import com.example.demo.domain.dto.response.Product.ProductDTO;
import com.example.demo.domain.dto.request.Product.AddProductRequest;
import com.example.demo.domain.dto.response.Product.GetAllProductResponse;
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
import java.util.List;

@WebServlet(urlPatterns = "/products/*")
public class ProductController extends HttpServlet {

    private ProductService productService;
    private ObjectMapper objectMapper;

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
        if ("/new".equals(path)) {
            addProduct(req, resp);
        } else {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/".equals(path)) {
            getAllProducts(resp);
        } else {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        }
    }

    private void addProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String authHeader = req.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                AddProductRequest addProductRequest = objectMapper.readValue(req.getInputStream(), AddProductRequest.class);
                ProductDTO productDTO = productService.addProduct(addProductRequest, token);
                ResponseUtil.sendSuccessResponse(resp, HttpServletResponse.SC_CREATED, "Add Product successful");
            } else {
                ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid");
            }
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void getAllProducts(HttpServletResponse resp) throws IOException {
        try {
            List<ProductDTO> products = productService.getAllProductList();
            GetAllProductResponse response = new GetAllProductResponse("Get All ProductList", HttpServletResponse.SC_OK, true, products);
            ResponseUtil.sendSuccessResponse(resp, response.getStatus(), response.getMessage());
        } catch (Exception e) {
            ResponseUtil.sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch products");
        }
    }
}