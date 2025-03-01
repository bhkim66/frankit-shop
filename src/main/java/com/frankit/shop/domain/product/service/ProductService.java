package com.frankit.shop.domain.product.service;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest.Create productCreateRequest) {
        return productRepository.save(productCreateRequest.toEntity());
    }
}
