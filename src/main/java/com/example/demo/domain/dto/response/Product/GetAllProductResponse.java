package com.example.demo.domain.dto.response.Product;

import com.example.demo.support.ApiResponse;

import java.util.List;

public class GetAllProductResponse {

    private List<ProductDTO> products;

    public GetAllProductResponse(List<ProductDTO> products) {
        this.products=products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
