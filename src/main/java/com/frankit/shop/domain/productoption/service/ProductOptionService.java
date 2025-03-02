package com.frankit.shop.domain.productoption.service;

import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import com.frankit.shop.domain.productoption.repository.ProductOptionRepository;
import com.frankit.shop.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.frankit.shop.global.exception.ExceptionEnum.OPTION_SIZE_OVER;
import static com.frankit.shop.global.exception.ExceptionEnum.OPTION_TYPE_IS_NOT_EQUALS;

@Service
@RequiredArgsConstructor
public class ProductOptionService {
    private final ProductOptionRepository productOptionRepository;

    public List<ProductOption> addProductOptions(List<ProductOptionRequest> productOptionRequests) {
        List<ProductOption> existedProductOptions = productOptionRepository.findAll();

        if(existedProductOptions.size() + productOptionRequests.size() > 3) {
            throw new ApiException(OPTION_SIZE_OVER);
        }
        if(!existedProductOptions.isEmpty() && existedProductOptions.getFirst().getType() != productOptionRequests.getFirst().getType()) {
            throw new ApiException(OPTION_TYPE_IS_NOT_EQUALS);
        }

        return productOptionRepository.saveAll(productOptionRequests.stream().map(ProductOptionRequest::toEntity).toList());
    }
}
