package com.frankit.shop.domain.product.api.controller;

import com.frankit.shop.domain.product.api.service.ProductService;
import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponseResult<Page<ProductResponse>>> getProducts(Pageable page) {
        return ResponseEntity.ok(ApiResponseResult.success(productService.getProducts(page)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseResult<Void>> createProducts(@RequestBody @Valid ProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseResult<Void>> updateProduct(@PathVariable Long id,@RequestBody @Valid ProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseResult<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseResult.success());
    }
}
