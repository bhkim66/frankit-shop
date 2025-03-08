package com.frankit.shop.domain.product.repository;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.global.condition.ProductCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductConditionRepository {
    Page<Product> findByProductsWithCondition(Pageable page, ProductCondition condition);
}
