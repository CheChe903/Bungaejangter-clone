package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.Product;
import com.example.demo.domain.dto.response.Product.ProductDTO;
import com.example.demo.domain.dto.request.Product.AddProductRequest;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.JwtUtil;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public ProductService(ProductRepository productRepository, MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public ProductDTO addProduct(AddProductRequest req, String token) {
        Member member = memberRepository.getMemberById(jwtUtil.parseToken(token));
        Product product = new Product(req.getProductName(), req.getDescription(), req.getPrice(), req.getStatus(), req.getImageUrl());
        product.setMember(member);
        productRepository.addProduct(product);
        return new ProductDTO(product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice(), product.getStatus(), product.getImageUrl());
    }

    public List<ProductDTO> getAllProductList() {
        return productRepository.getAllProductList();
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.getProductById(productId);
        return new ProductDTO(product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice(), product.getStatus(), product.getImageUrl());
    }
}
