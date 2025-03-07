package com.frankit.shop.domain.product.repository;

import com.frankit.shop.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.delYn = 'N'")
    Page<Product> findByListIdAndDelYnN(Pageable page);

    @Query("select p from Product p where p.id = :id and p.delYn = 'N'")
    Optional<Product> findByIdAndDelYnN(Long id);
}
