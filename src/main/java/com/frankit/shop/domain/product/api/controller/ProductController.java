package com.frankit.shop.domain.product.api.controller;

import com.frankit.shop.domain.product.api.service.ProductService;
import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "product", description = "product 컨트롤러에 대한 설명입니다.")
@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "여러개의 상품을 페이징 조회힌다", description = "상품 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponseResult<Page<ProductResponse>>> selectProducts(Pageable page) {
        return ResponseEntity.ok(ApiResponseResult.success(productService.selectProducts(page)));
    }

    @Operation(summary = "하나의 상품을 조회힌다", description = "상품 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseResult<ProductResponse>> selectProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(ApiResponseResult.success(productService.selectProduct(productId)));
    }

    @Operation(summary = "하나의 상품을 생성한다", description = "상품 생성")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> createProducts(@RequestBody @Valid ProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @Operation(summary = "하나의 상품을 수정한다", description = "상품 수정")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> updateProduct(@PathVariable Long id,@RequestBody @Valid ProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @Operation(summary = "하나의 상품을 삭제한다", description = "상품 삭제")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseResult.success());
    }
}
