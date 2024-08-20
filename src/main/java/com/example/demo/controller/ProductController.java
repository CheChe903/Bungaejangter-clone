package com.example.demo.controller;

import com.example.demo.domain.dto.response.Product.ProductDTO;
import com.example.demo.domain.dto.request.Product.AddProductRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.ResponseUtil;
import com.example.demo.support.ApiResponse;
import com.example.demo.support.ApiResponseGenerator;
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
        ApiResponse<?> apiResponse;

        if ("/new".equals(path)) {
            apiResponse = addProduct(req);
        } else {
            apiResponse = ApiResponseGenerator.fail("Invalid path", HttpServletResponse.SC_BAD_REQUEST);
        }

        ResponseUtil.sendResponse(resp, apiResponse);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        ApiResponse<?> apiResponse;

        if ("/".equals(path)) {
            apiResponse = getAllProducts();
        } else {
            apiResponse = ApiResponseGenerator.fail("Invalid path", HttpServletResponse.SC_BAD_REQUEST);
        }

        ResponseUtil.sendResponse(resp, apiResponse);
    }

    private ApiResponse<?> addProduct(HttpServletRequest req) throws IOException {
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ApiResponseGenerator.fail("Authorization header is missing or invalid", HttpServletResponse.SC_UNAUTHORIZED);
        }

        try {
            String token = authHeader.substring(7);
            AddProductRequest addProductRequest = objectMapper.readValue(req.getInputStream(), AddProductRequest.class);
            ProductDTO productDTO = productService.addProduct(addProductRequest, token);
            return ApiResponseGenerator.success(productDTO, HttpServletResponse.SC_CREATED, "Add Product successful", "PRODUCT_ADDED");
        } catch (Exception e) {
            return ApiResponseGenerator.fail("Internal server error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private ApiResponse<?> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProductList();
            return ApiResponseGenerator.success(products, HttpServletResponse.SC_OK, "Get All ProductList", "PRODUCTS_FETCHED");
        } catch (Exception e) {
            return ApiResponseGenerator.fail("Failed to fetch products", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}