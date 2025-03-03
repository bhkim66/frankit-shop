package com.frankit.shop.domain.auth.service;

import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.user.entity.User;
import com.frankit.shop.domain.user.repository.UserRepository;
import com.frankit.shop.global.exception.ApiException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.frankit.shop.global.exception.ExceptionEnum.USERNAME_NOT_FOUND_EXCEPTION;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findById(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new ApiException(USERNAME_NOT_FOUND_EXCEPTION));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private CustomUserDetail createUserDetails(User user) {
       return new CustomUserDetail(user);
    }
}
