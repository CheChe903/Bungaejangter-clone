package com.example.demo.repository;


import com.example.demo.domain.Product;
import com.example.demo.domain.dto.response.Product.ProductDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private static ProductRepository instance;

    private final Map<Long, Product> productMap;

    private long nextId;

    public ProductRepository() {
        productMap = new HashMap<>();
        nextId=1L;
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }
    public synchronized void addProduct(Product product) {
        product.setProductId(nextId++);
        productMap.put(product.getProductId(), product);
    }

    public synchronized List<ProductDTO> getAllProductList() {
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

    public synchronized Product getProductById(Long productId) {
        return productMap.get(productId);
    }
    public  synchronized List<Product> getProductListById(Long memberId) {
        List<Product> products = new ArrayList<>();
        for (Product product : productMap.values()) {
            if (product.getMember() != null && product.getMember().getMemberId().equals(memberId)) {
                products.add(product);
            }
        }
        return products;
    }
}
