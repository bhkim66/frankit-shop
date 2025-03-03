package com.frankit.shop.domain.productoption.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.frankit.shop.domain.productoption.common.SelectTypeOption.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SelectTypeOptionTest {

    @DisplayName("상품 옵션을 등록할 때 선택 타입시 사전에 정의돈 값들을 조회할 수 있다")
    @Test
    void selectProductOptionTypeList() {
        List<SelectTypeOption> values = List.of(values());

        assertThat(values).contains(M, L, S, FREE);
    }
}