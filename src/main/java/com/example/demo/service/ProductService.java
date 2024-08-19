package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.Product;
import com.example.demo.domain.dto.ProductDTO;
import com.example.demo.domain.dto.request.Product.AddProductRequest;
import com.example.demo.domain.dto.response.Member.RegisterResponse;
import com.example.demo.domain.dto.response.Product.AddProductResponse;
import com.example.demo.domain.dto.response.Product.GetAllProductResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    private final JwtUtil jwtUtil;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.jwtUtil =jwtUtil;
    }


    public AddProductResponse addProduct(AddProductRequest req, String token) {
        Member member = memberRepository.getMemberById(jwtUtil.parseToken(token));
        Product product = new Product(req.getProductName(), req.getDescription(),req.getPrice(),req.getStatus(),req.getImageUrl());
        product.setMember(member);
        productRepository.addProduct(product);
        return new AddProductResponse("Add Product successful", HttpServletResponse.SC_CREATED, true);
    }

    public GetAllProductResponse getAllProductList() {
        List<ProductDTO> productDTOList = productRepository.getAllProductList();
        return new GetAllProductResponse(("Get All ProductList"), HttpServletResponse.SC_OK,true, productDTOList);
    }
}
