package com.example.demo.domain;

import com.example.demo.domain.dto.response.Product.ProductDTO;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private Long memberId;
    private String username;
    private String email;
    private String hashedPassword;
    private List<Product> products;

    public Member() {}

    public Member(String username, String email, String hashedPassword) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.products = new ArrayList<>();
    }


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products= products;
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

    public String getPassword() {
        return hashedPassword;
    }

    public void setPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

}
