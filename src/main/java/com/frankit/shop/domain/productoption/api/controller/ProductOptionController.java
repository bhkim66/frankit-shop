package com.frankit.shop.domain.productoption.api.controller;

import com.frankit.shop.domain.productoption.api.service.ProductOptionService;
import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.dto.ProductOptionResponse;
import com.frankit.shop.global.common.ApiResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/product-option")
@RequiredArgsConstructor
public class ProductOptionController {
    private final ProductOptionService productOptionService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseResult<List<ProductOptionResponse>>> selectProductOptions(@PathVariable(value = "productId") Long productId) {
        return ResponseEntity.ok(ApiResponseResult.success(productOptionService.selectProductOptions(productId)));
    }

    @PostMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> addProductOptions(@PathVariable("productId") Long productId, @RequestBody List<ProductOptionRequest> productOptionRequests) {
        productOptionService.addProductOptions(productId, productOptionRequests);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> updateProductOption(@PathVariable("id") Long id, @RequestBody ProductOptionRequest productOptionRequest){
        productOptionService.updateProductOption(id, productOptionRequest);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseResult<Void>> deleteProductOption(@PathVariable("id") Long id) {
        productOptionService.deleteProductOption(id);
        return ResponseEntity.ok(ApiResponseResult.success());
    }

}
