package com.example.demo.domain.dto.response.Product;

import com.example.demo.domain.dto.ProductDTO;
import com.example.demo.domain.dto.response.ApiResponse;

import java.math.BigDecimal;
import java.util.List;

public class GetAllProductResponse extends ApiResponse {

    private List<ProductDTO> products;

    public GetAllProductResponse(String message, int status, boolean success, List<ProductDTO> products) {
        super(message, status, success);
        this.products=products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
