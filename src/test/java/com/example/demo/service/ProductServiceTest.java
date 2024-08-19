package com.example.demo.service;

import com.example.demo.domain.dto.response.Product.ProductDTO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        MemberRepository memberRepository = mock(MemberRepository.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        productService = new ProductService(productRepository, memberRepository, jwtUtil);
    }

    @Test
    void testGetAllProductList() {
        // Arrange
        ProductDTO product1 = new ProductDTO(1L,"Product1", "Description1", new BigDecimal(50.0), "Available", "imageUrl1");
        ProductDTO product2 = new ProductDTO(2L,"Product2", "Description2", new BigDecimal(50.0), "Available", "imageUrl2");
        List<ProductDTO> productList = Arrays.asList(product1, product2);

        when(productRepository.getAllProductList()).thenReturn(productList);

        // Act
        List<ProductDTO> productDTOList = productService.getAllProductList();

        // Assert
        assertNotNull(productDTOList);
        assertEquals(2, productDTOList.size());
        assertEquals("Product1", productDTOList.get(0).getProductName());
        assertEquals("Product2", productDTOList.get(1).getProductName());
    }
}