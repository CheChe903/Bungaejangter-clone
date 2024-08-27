package com.example.demo.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {

    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private String imageUrl;

    private String title;
    private Member member;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Product(String productName, String description, BigDecimal price, ProductStatus status, String imageUrl, String title, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.title = title;
        this.createdAt =createdAt;
        this.updateAt = updateAt;
    }

    public Product() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
