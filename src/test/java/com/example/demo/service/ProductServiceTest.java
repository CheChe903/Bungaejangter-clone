package com.example.demo.service;

import com.example.demo.domain.Product;
import com.example.demo.domain.ProductStatus;
import com.example.demo.domain.dto.response.Product.ProductDTO;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        ProductDTO product1 = new ProductDTO(1L,"Product1", "Description1", new BigDecimal("50.0"), ProductStatus.AVAILABLE, "imageUrl1", "title1", LocalDateTime.now(), LocalDateTime.now());
        ProductDTO product2 = new ProductDTO(2L,"Product2", "Description2", new BigDecimal("50.0"), ProductStatus.AVAILABLE, "imageUrl2", "title2", LocalDateTime.now(), LocalDateTime.now());
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

    @Test
    void testGetProductById() {
        // Arrange
        Product product = new Product("Product1", "Description1", new BigDecimal("50.0"), ProductStatus.AVAILABLE, "imageUrl1", "title1", LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.getProductById(1L)).thenReturn(product);

        // Act
        ProductDTO productDTO = productService.getProductById(1L);

        // Assert
        assertNotNull(productDTO);
        assertEquals("Product1", productDTO.getProductName());
    }
}