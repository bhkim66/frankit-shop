package com.frankit.shop.domain.product.repository;

import com.frankit.shop.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
