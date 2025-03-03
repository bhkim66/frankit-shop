package com.frankit.shop.domain.productoption.repository;

import com.frankit.shop.domain.productoption.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @Query("select po from ProductOption po join po.product p where p.id = :productId and po.delYn = 'N'")
    List<ProductOption> findProductOptions(Long productId);

    @Query("select po from ProductOption po where po.id = :productOptionId and po.delYn = 'N'")
    Optional<ProductOption> findProductOption(Long productOptionId);
}
