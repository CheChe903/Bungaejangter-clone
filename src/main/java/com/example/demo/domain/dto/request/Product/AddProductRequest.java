package com.example.demo.domain.dto.request.Product;

import java.math.BigDecimal;

public class AddProductRequest {
    private String productName;
    private String description;
    private BigDecimal price;
    private String status;
    private String imageUrl;

    public AddProductRequest() {
    }
    public AddProductRequest(String productName, String description, BigDecimal price, String status, String imageUrl) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
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