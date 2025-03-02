package com.frankit.shop.domain.productoption.service;

import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import com.frankit.shop.domain.productoption.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;

    public List<ProductOption> addProductOptions(List<ProductOptionRequest> productOptionRequests) {
        List<ProductOption> existedProductOptions = productOptionRepository.findAll();

        if(existedProductOptions.size() + productOptionRequests.size() > 3) {
            throw new IllegalArgumentException("3개 이하의 상품 옵션만 등록할 수 있습니다.");
        }

        return productOptionRepository.saveAll(productOptionRequests.stream().map(ProductOptionRequest::toEntity).toList());
    }
}
