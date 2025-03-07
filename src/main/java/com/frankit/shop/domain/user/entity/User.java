package com.frankit.shop.domain.user.entity;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.global.common.TypeEnum;
import com.frankit.shop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "users")
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Builder
    private User(String email, RoleEnum role) {
        this.email = email;
        this.role = role;
    }

    public static User of(String email, RoleEnum role) {
        return User.builder()
                .email(email)
                .role(role)
                .build();
    }
}
