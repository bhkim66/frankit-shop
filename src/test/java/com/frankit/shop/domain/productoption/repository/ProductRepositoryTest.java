package com.frankit.shop.domain.productoption.repository;

import com.frankit.shop.domain.config.DatabaseCleanUp;
import com.frankit.shop.domain.product.entity.Product;
import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.frankit.shop.domain.productoption.common.OptionType.I;

@ActiveProfiles("test")
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductOptionRepository productOptionRepository;

    @Autowired
    DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        //given
        ProductOption option1 = createProductOptionStep(null,"스몰", I, 0);
        ProductOption option2 = createProductOptionStep(null, "미디움", I, 500);
        ProductOption option3 = createProductOptionStep(null, "라지", I, 1000);
        //when
        productOptionRepository.saveAll(List.of(option1, option2, option3));
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.afterPropertiesSet();
        databaseCleanUp.execute();
    }


    private static ProductOption createProductOptionStep(Product product, String optionName, OptionType optionType, int price) {
        return ProductOption.create(product, optionName, optionType, price);
    }
}
