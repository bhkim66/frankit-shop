package com.frankit.shop.domain.productoption.service;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductRepository;
import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.dto.ProductOptionResponse;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import com.frankit.shop.domain.productoption.repository.ProductOptionRepository;
import com.frankit.shop.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.frankit.shop.global.exception.ExceptionEnum.*;

/**
 * 상품 옵션 service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    public List<ProductOptionResponse> findProductOptions(Long productId) {
        return productOptionRepository.findProductOptions(productId)
                .stream().map(ProductOptionResponse::of).toList();
    }

    @Transactional
    public List<ProductOptionResponse> addProductOptions(Long productId, List<ProductOptionRequest> productOptionRequests) {
        List<ProductOption> existedProductOptions = productOptionRepository.findProductOptions(productId);

        // 상품 옵션은 최대 3개까지만 등록 가능하다.
        if (existedProductOptions.size() + productOptionRequests.size() > 3) {
            throw new ApiException(OPTION_SIZE_OVER);
        }
        // 상품 옵션 타입은 기존 옵션 타입과 동일한지 체크
        if (!existedProductOptions.isEmpty()) {
            productOptionRequests.forEach(optionRequest -> isOptionTypeEquals(existedProductOptions.getFirst().getType(), optionRequest.getType()));
        }
        Product product = productRepository.getReferenceById(productId);
        return productOptionRepository.saveAll(productOptionRequests
                    .stream().map(request -> request.toEntity(product)).toList())
                    .stream().map(ProductOptionResponse::of).toList();
    }

    @Transactional
    public ProductOption updateProductOption(Long productOptionId, ProductOptionRequest productOptionRequest) {
        ProductOption productOption = productOptionRepository.findProductOption(productOptionId).orElseThrow(() -> new ApiException(NOT_FOUND_ERROR));
        // 상품 옵션 타입은 기존 옵션 타입과 동일한지 체크
        isOptionTypeEquals(productOption.getType(), productOptionRequest.getType());

        return productOptionRepository.findProductOption(productOptionId)
                .orElseThrow(() -> new ApiException(NOT_FOUND_ERROR))
                .update(productOptionRequest);
    }

    @Transactional
    public ProductOption deleteProductOption(Long productOptionId) {
        return productOptionRepository.findProductOption(productOptionId)
                .orElseThrow(() -> new ApiException(NOT_FOUND_ERROR))
                .delete();
    }


    /**
     * 상품 옵션 타입은 기존 옵션 타입과 동일한지 체크
     */
    private void isOptionTypeEquals(OptionType existType, OptionType addType) {
        if (!existType.equals(addType)) {
            throw new ApiException(OPTION_TYPE_IS_NOT_EQUALS);
        }
    }
}
