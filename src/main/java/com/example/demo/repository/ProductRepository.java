package com.example.demo.repository;


import com.example.demo.domain.Product;
import com.example.demo.domain.dto.ProductDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();

    private long nextId = 1;

    public void addProduct(Product product) {
        product.setProductId(nextId++);
        productMap.put(product.getProductId(), product);
    }

    public List<ProductDTO> getAllProductList() {
        List<ProductDTO> products = new ArrayList<>();
        for(Map.Entry<Long, Product> entry :productMap.entrySet()) {
            Product product = entry.getValue();

            ProductDTO productDTO = new ProductDTO(
                    product.getProductId(),
                    product.getProductName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStatus(),
                    product.getImageUrl()
            );

            products.add(productDTO);
        }
        return products;
    }
}
