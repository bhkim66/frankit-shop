package com.frankit.shop.domain.productoption;

import com.frankit.shop.domain.productoption.common.OptionType;
import com.frankit.shop.domain.productoption.dto.ProductOptionRequest;
import com.frankit.shop.domain.productoption.entity.ProductOption;
import com.frankit.shop.domain.productoption.repository.ProductOptionRepository;
import com.frankit.shop.domain.productoption.service.ProductOptionService;
import com.frankit.shop.global.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;

import static com.frankit.shop.domain.productoption.common.OptionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductOptionServiceTest {

    @Mock
    ProductOptionRepository productOptionRepository;

    @InjectMocks
    ProductOptionService productOptionService;


    @DisplayName("3개 이하의 입력 타입 상품 옵션을 등록할 수 있다")
    @Test
    void addProductOptionsInputType() {
        //given
        ProductOption option1 = createProductOptionStep("스몰", I, 0);
        ProductOption option2 = createProductOptionStep("미디움", I, 500);
        ProductOption option3 = createProductOptionStep("라지", I, 1000);

        when(productOptionRepository.saveAll(anyList())).thenReturn(List.of(option1, option2, option3));

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("스몰", I, 0),
                ProductOptionRequest.of("미디움", I, 500),
                ProductOptionRequest.of("라지", I, 1000)
        );

        //when
        List<ProductOption> productOptions = productOptionService.addProductOptions(productOptionRequests);

        //then
        assertThat(productOptions).hasSize(3)
                .extracting("name", "type", "extraPrice")
                .containsExactlyInAnyOrder(
                        tuple("스몰", I, 0),
                        tuple("미디움", I, 500),
                        tuple("라지", I, 1000)
                );
        verify(productOptionRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("3개 이하의 선택 타입 상품 옵션을 등록할 수 있다")
    @Test
    void addProductOptionsSelectType() {
        //given
        ProductOption option1 = createProductOptionStep("S", S, 0);
        ProductOption option2 = createProductOptionStep("M", S, 500);
        ProductOption option3 = createProductOptionStep("L", S, 1000);

        when(productOptionRepository.saveAll(anyList())).thenReturn(List.of(option1, option2, option3));

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("S", S, 0),
                ProductOptionRequest.of("M", S, 500),
                ProductOptionRequest.of("L", S, 1000)
        );

        //when
        List<ProductOption> productOptions = productOptionService.addProductOptions(productOptionRequests);

        //then
        assertThat(productOptions).hasSize(3)
                .extracting("name", "type", "extraPrice")
                .containsExactlyInAnyOrder(
                        tuple("S", S, 0),
                        tuple("M", S, 500),
                        tuple("L", S, 1000)
                );
        verify(productOptionRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("상품의 상품 옵션 갯수가 3개를 넘으면 예외가 발생한다")
    @Test
    void addProductTotalOptionsLessThenThree() {
        //given
        ProductOption option1 = createProductOptionStep("S", S, 0);
        ProductOption option2 = createProductOptionStep("M", S, 500);
        ProductOption option3 = createProductOptionStep("L", S, 1000);

        when(productOptionRepository.findAll()).thenReturn(List.of(option1, option2, option3));

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("XL", S, 0)
        );

        //when & then
        assertThatThrownBy(() -> productOptionService.addProductOptions(productOptionRequests))
                .isInstanceOf(ApiException.class)
                .hasMessage("3개 이하의 상품 옵션만 등록할 수 있습니다.");

        verify(productOptionRepository, times(1)).findAll();
        verify(productOptionRepository, never()).saveAll(anyList());
    }

    @DisplayName("상품의 상품 옵션은 기존 옵션과 다르면 예외가 발생한다")
    @Test
    void addProductOptionsSameExistOptionType() {
        //given
        ProductOption option1 = createProductOptionStep("S", S, 0);

        when(productOptionRepository.findAll()).thenReturn(List.of(option1));

        List<ProductOptionRequest> productOptionRequests = List.of(
                ProductOptionRequest.of("XL", I, 1000)
        );

        //when & then
        assertThatThrownBy(() -> productOptionService.addProductOptions(productOptionRequests))
                .isInstanceOf(ApiException.class)
                .hasMessage("기존 옵션 타입과 다른 옵션 타입을 등록할 수 없습니다.");

        verify(productOptionRepository, times(1)).findAll();
        verify(productOptionRepository, never()).saveAll(anyList());
    }




    private static ProductOption createProductOptionStep(String optionName, OptionType optionType, int price) {
        return ProductOption.create(optionName, optionType, price);
    }

}
