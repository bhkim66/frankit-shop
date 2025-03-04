package com.frankit.shop.domain.auth.facade;

import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.global.exception.ApiException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.frankit.shop.global.exception.ExceptionEnum.INVALID_TOKEN_VALUE_ERROR;


@Component
public class AuthFacade {
    private CustomUserDetail getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(INVALID_TOKEN_VALUE_ERROR);
        }
        return (CustomUserDetail) authentication.getPrincipal();
    }

    public String getCurrentUserEmail() {
        return getCurrentUserDetails().getEmail();
    }
}
