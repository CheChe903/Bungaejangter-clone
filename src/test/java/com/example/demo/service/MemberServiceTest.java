package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.domain.Product;
import com.example.demo.domain.ProductStatus;
import com.example.demo.domain.dto.request.Member.MemberLoginRequest;
import com.example.demo.domain.dto.request.Member.MemberRegisterRequest;
import com.example.demo.domain.dto.response.Member.MemberDTO;
import com.example.demo.domain.dto.response.Product.ProductDTO;
import com.example.demo.repository.MemberRepository;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private PasswordUtil passwordUtil;
    private JwtUtil jwtUtil;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        passwordUtil = mock(PasswordUtil.class);
        jwtUtil = mock(JwtUtil.class);
        memberService = new MemberService(memberRepository, passwordUtil, jwtUtil);
    }

    @Test
    void testRegisterMember_Success() {
        // Arrange
        MemberRegisterRequest registerRequest = new MemberRegisterRequest("username", "email@example.com", "password");
        when(memberRepository.getMemberByEmail(registerRequest.getEmail())).thenReturn(null);
        when(passwordUtil.hashPassword(registerRequest.getPassword())).thenReturn("hashedPassword");

        // Act
        boolean result = memberService.registerMember(registerRequest);

        // Assert
        assertTrue(result);
        verify(memberRepository, times(1)).addMember(any(Member.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        MemberLoginRequest loginRequest = new MemberLoginRequest("email@example.com", "password");
        Member member = new Member("username", "email@example.com", "hashedPassword");
        when(memberRepository.getMemberByEmail(loginRequest.getEmail())).thenReturn(member);
        when(passwordUtil.hashPassword(loginRequest.getPassword())).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(member.getMemberId())).thenReturn("jwtToken");

        // Act
        String token = memberService.login(loginRequest);

        // Assert
        assertNotNull(token);
        assertEquals("jwtToken", token);
    }

    @Test
    void testGetMemberById() {
        // Arrange
        Long memberId = 1L;
        Member member = getMember(memberId);

        when(memberRepository.getMemberById(memberId)).thenReturn(member);

        // Act
        MemberDTO memberDTO = memberService.getMemberById(memberId);

        // Assert
        assertNotNull(memberDTO);
        assertEquals(memberId, memberDTO.getMemberId());
        assertEquals("username", memberDTO.getUsername());
        assertEquals("email@example.com", memberDTO.getEmail());

        List<ProductDTO> productDTOs = memberDTO.getProducts();
        assertEquals(2, productDTOs.size());

        ProductDTO productDTO1 = productDTOs.get(0);
        assertEquals(1L, productDTO1.getProductId());
        assertEquals("Product1", productDTO1.getProductName());
        assertEquals("Description1", productDTO1.getDescription());
        assertEquals(new BigDecimal("100.0"), productDTO1.getPrice());
        assertEquals(ProductStatus.AVAILABLE, productDTO1.getStatus());
        assertEquals("imageUrl1", productDTO1.getImageUrl());

        ProductDTO productDTO2 = productDTOs.get(1);
        assertEquals(2L, productDTO2.getProductId());
        assertEquals("Product2", productDTO2.getProductName());
        assertEquals("Description2", productDTO2.getDescription());
        assertEquals(new BigDecimal("200.0"), productDTO2.getPrice());
        assertEquals(ProductStatus.AVAILABLE, productDTO2.getStatus());
        assertEquals("imageUrl2", productDTO2.getImageUrl());
    }


    @Test
    void testGetProductsListSortedStatus() {
        // Arrange
        Product product1 = new Product("Product1", "Description1", new BigDecimal("100.0"), ProductStatus.SOLD_OUT, "imageUrl1");
        Product product2 = new Product("Product2", "Description2", new BigDecimal("200.0"), ProductStatus.AVAILABLE, "imageUrl2");
        Product product3 = new Product("Product3", "Description3", new BigDecimal("300.0"), ProductStatus.AVAILABLE, "imageUrl3");
        product1.setProductId(1L);
        product2.setProductId(2L);
        product3.setProductId(3L);

        List<Product> sortedProducts = Arrays.asList(product2, product3, product1);

        when(memberRepository.getProductsListSortedStatus(ProductStatus.AVAILABLE)).thenReturn(sortedProducts);

        // Act
        List<ProductDTO> productDTOs = memberService.getProductsListSortedStatus(ProductStatus.AVAILABLE);

        // Assert
        assertNotNull(productDTOs);
        assertEquals(3, productDTOs.size());

        assertEquals(2L, productDTOs.get(0).getProductId());  // Product2 with AVAILABLE
        assertEquals(3L, productDTOs.get(1).getProductId());  // Product3 with AVAILABLE
        assertEquals(1L, productDTOs.get(2).getProductId());  // Product1 with SOLD_OUT
    }


    private static Member getMember(Long memberId) {
        Member member = new Member("username", "email@example.com", "hashedPassword");
        member.setMemberId(memberId);

        Product product1 = new Product("Product1", "Description1", new BigDecimal("100.0"), ProductStatus.AVAILABLE, "imageUrl1");
        product1.setProductId(1L);
        Product product2 = new Product("Product2", "Description2", new BigDecimal("200.0"), ProductStatus.AVAILABLE, "imageUrl2");
        product2.setProductId(2L);

        List<Product> products = Arrays.asList(product1, product2);
        member.setProducts(products);
        return member;
    }
}