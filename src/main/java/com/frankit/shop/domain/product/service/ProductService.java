package com.frankit.shop.domain.product.service;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.frankit.shop.global.exception.ExceptionEnum.NOT_FOUND_ERROR;

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
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(NOT_FOUND_ERROR))
                .update(productRequest);
    }

    @Transactional
    public Product deleteProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(NOT_FOUND_ERROR))
                .delete();
    }
}
