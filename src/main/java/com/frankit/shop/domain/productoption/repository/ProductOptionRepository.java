package com.frankit.shop.domain.productoption.repository;

import com.frankit.shop.domain.productoption.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @Query("select po from ProductOption po where po.product.id = :productId and delYn = 'N'")
    List<ProductOption> findProductOptions(Long productId);

    Optional<ProductOption> findProductOption(Long productOptionId);
}
