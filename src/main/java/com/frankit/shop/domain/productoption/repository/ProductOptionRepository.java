package com.frankit.shop.domain.productoption.repository;

import com.frankit.shop.domain.productoption.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
