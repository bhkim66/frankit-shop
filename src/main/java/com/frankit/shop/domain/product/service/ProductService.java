package com.frankit.shop.domain.product.service;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductResponse> getProducts(Pageable page) {
        return productRepository.findAll(page).map(ProductResponse::of);
    }

    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        return productRepository.save(productRequest.toEntity());
    }

    @Transactional
    public Product updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        return product.update(productRequest);
    }

    @Transactional
    public Product deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
        return product.delete();
    }
}
