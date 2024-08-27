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
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, PasswordUtil passwordUtil, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    public boolean registerMember(MemberRegisterRequest registerRequest) {
        if (memberRepository.getMemberByEmail(registerRequest.getEmail()) != null) {
            return false;
        }

        String hashedPassword = passwordUtil.hashPassword(registerRequest.getPassword());
        Member newMember = new Member(registerRequest.getUsername(), registerRequest.getEmail(), hashedPassword);
        memberRepository.addMember(newMember);

        return true;
    }

    public String login(MemberLoginRequest loginRequest) {
        Member member = memberRepository.getMemberByEmail(loginRequest.getEmail());

        if (member == null) {
            return null;
        }

        String inputPassword = passwordUtil.hashPassword(loginRequest.getPassword());

        if (Objects.equals(inputPassword, member.getPassword())) {
            return jwtUtil.generateToken(member.getMemberId());
        } else {
            return null;
        }
    }

    public MemberDTO getMemberById(Long memberId) {
        Member member = memberRepository.getMemberById(memberId);
        List<Product> products = member.getProducts();
        List<ProductDTO> productDTOs = products.stream()
                .map(product -> new ProductDTO(product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice(), product.getStatus(), product.getImageUrl()))
                .collect(Collectors.toList());
        return new MemberDTO(member.getMemberId(), member.getUsername(), member.getEmail(), productDTOs);
    }

    public List<ProductDTO> getProductsListSortedStatus(ProductStatus status) {
        List<Product> products = memberRepository.getProductsListSortedStatus(status);
        return products.stream()
                .sorted((p1, p2) -> {
                    if (p1.getStatus() == p2.getStatus()) {
                        return p1.getProductId().compareTo(p2.getProductId());
                    } else if (p1.getStatus() == ProductStatus.AVAILABLE) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .map(product -> new ProductDTO(
                        product.getProductId(),
                        product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getImageUrl()))
                .collect(Collectors.toList());
    }
}
