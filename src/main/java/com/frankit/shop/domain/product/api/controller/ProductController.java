package com.frankit.shop.domain.product.api.controller;

import com.frankit.shop.domain.product.api.service.ProductService;
import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.dto.ProductResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import com.frankit.shop.global.condition.ProductCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.hibernate.dialect.function.InverseDistributionFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

@RestController
@RequestMapping("/v1/product")
@RequiredArgsConstructor
public class ProductController implements ProductApiSpecification {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponseResult<Page<ProductResponse>>> selectProductsWithCondition(ProductCondition condition, Pageable page) {
        return ResponseEntity.ok(ApiResponseResult.success(productService.selectProductsCondition(page, condition)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseResult<ProductResponse>> selectProduct(@PathVariable("id") Long productId) {
        return ResponseEntity.ok(ApiResponseResult.success(productService.selectProduct(productId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> createProducts(@RequestBody @Valid ProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> updateProduct(@PathVariable Long id,@RequestBody @Valid ProductRequest request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponseResult.success());
    }
}
