package com.example.demo.repository;


import com.example.demo.domain.Product;

import java.util.HashMap;
import java.util.Map;

public class ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();

    private long nextId = 1;

    public void addProduct(Product product) {
        product.setProductId(nextId++);
        productMap.put(product.getProductId(), product);
    }
}
