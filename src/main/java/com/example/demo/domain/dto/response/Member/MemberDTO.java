package com.example.demo.domain.dto.response.Member;

import com.example.demo.domain.dto.response.Product.ProductDTO;

import java.util.List;

public class MemberDTO {
    private Long memberId;
    private String username;
    private String email;

    private List<ProductDTO> productDTOList;

    public MemberDTO() {
    }

    public MemberDTO(Long memberId, String username, String email, List<ProductDTO> productDTOList) {
        this.memberId = memberId;
        this.username = username;
        this.email = email;
        this.productDTOList= productDTOList;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProductDTO> getProductDTOList() {
        return productDTOList;
    }

    public void setProductDTOList(List<ProductDTO> productDTOList) {
        this.productDTOList = productDTOList;
    }

    public List<ProductDTO> getProducts() {
        return  productDTOList;
    }
}
