package com.frankit.shop.global.entity;

import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;

@MappedSuperclass
@EnableJpaAuditing
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;
}
