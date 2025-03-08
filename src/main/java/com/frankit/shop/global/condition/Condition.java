package com.frankit.shop.global.condition;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@SuperBuilder
public class Condition {
    private String name;
    private LocalDateTime createdAt;
}
