package com.frankit.shop.domain.user.entity;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Builder
    private Users(String email, RoleEnum role) {
        this.email = email;
        this.role = role;
    }

    public static Users of(String email, RoleEnum role) {
        return Users.builder()
                .email(email)
                .role(role)
                .build();
    }
}
