package com.frankit.shop.domain.user.repository;

import com.frankit.shop.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where email = :email")
    Optional<User> findById(String email);
}
