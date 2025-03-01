package com.frankit.shop.domain.product.repository;

import com.frankit.shop.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.delYn = 'N'")
    Page<Product> findByDelYn(Pageable page);
}
