package com.frankit.shop.domain.user.repository;

import com.frankit.shop.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("select u from Users u where email = :email")
    Optional<Users> findById(String email);
}
