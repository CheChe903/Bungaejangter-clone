package com.example.demo.domain.dto;

import java.math.BigDecimal;

public class ProductDTO {

    private Long productId;
    private String productName;
    private BigDecimal price;
    private String status;

    private String description;
    private String imageUrl;

    public ProductDTO() {

    }

    public ProductDTO(Long productId, String productName, String description, BigDecimal price, String status, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.description =description;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


