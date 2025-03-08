package com.frankit.shop.domain.product.repository.impl;

import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.product.repository.ProductConditionRepository;
import com.frankit.shop.global.condition.ProductCondition;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.frankit.shop.domain.product.entity.QProduct.product;
import static com.frankit.shop.domain.productoption.entity.QProductOption.productOption;
import static com.frankit.shop.global.common.TypeEnum.N;

@RequiredArgsConstructor
public class ProductConditionRepositoryImpl implements ProductConditionRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findByProductsWithCondition(Pageable page, ProductCondition condition) {
        List<Product> content = queryFactory
                .selectFrom(product)
                .leftJoin(product.productOptions, productOption)
                .where(nameContain(condition.getName()),
                        priceLoe(condition.getEndPrice()),
                        priceGoe(condition.getStartPrice()),
                        delYnIsN()
                )
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

        JPAQuery<Long> count = queryFactory
                .select(product.count())
                .from(product)
                .where(nameContain(condition.getName()),
                        priceLoe(condition.getEndPrice()),
                        priceGoe(condition.getStartPrice()),
                        delYnIsN());

        return PageableExecutionUtils.getPage(content, page, count::fetchOne);
    }

    // 금액이 조건보다 큰 경우
    private BooleanExpression priceGoe(int price) {
        return price > 0 ? product.price.goe(price) : null;

    }

    // 금액이 조건보다 작은 경우
    private BooleanExpression priceLoe(int price) {
        return price > 0 ? product.price.loe(price) : null;
    }

    // 이름이 일치하는지
    private BooleanExpression nameContain(String name) {
        return name != null ? product.name.contains(name) : null;
    }

    // 삭제되지 않는 데이터 출력
    private BooleanExpression delYnIsN() {
        return product.delYn.eq(N);
    }
}
