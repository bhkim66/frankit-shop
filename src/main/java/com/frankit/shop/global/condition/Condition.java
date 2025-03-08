package com.frankit.shop.global.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Condition {
    private String name;
    private LocalDateTime createdAt;
}
