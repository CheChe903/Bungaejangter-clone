package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.Product;
import com.example.demo.domain.dto.request.Product.AddProductRequest;
import com.example.demo.domain.dto.response.Product.AddProductResponse;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct_Success() {
        // Arrange
        String token = "valid-token";
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        AddProductRequest request = new AddProductRequest(
                "kimjinyeong",
                "hi",
                BigDecimal.valueOf(699.99),
                "판매중",
                "https://xxxx");

        when(jwtUtil.parseToken(token)).thenReturn(memberId);
        when(memberRepository.getMemberById(memberId)).thenReturn(member);

        // Act
        AddProductResponse response = productService.addProduct(request, token);

        // Assert
        verify(productRepository).addProduct(any(Product.class));
        assertEquals("Add Product successful", response.getMessage());
        assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
        assertEquals(true, response.isSuccess());
    }

    @Test
    void testAddProduct_TokenInvalid() {
        // Arrange
        String token = "invalid-token";
        AddProductRequest request = new AddProductRequest(
                "kimjinyeong",
                "hi",
                BigDecimal.valueOf(699.99),
                "판매중",
                "https://xxxx");

        when(jwtUtil.parseToken(token)).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        try {
            productService.addProduct(request, token);
        } catch (Exception e) {
            assertEquals("Invalid token", e.getMessage());
        }
    }
}
