package com.frankit.shop.domain.auth.api.service;

import com.frankit.shop.domain.auth.common.RoleEnum;
import com.frankit.shop.domain.auth.entity.CustomUserDetail;
import com.frankit.shop.domain.user.entity.Users;
import com.frankit.shop.domain.user.repository.UserRepository;
import com.frankit.shop.global.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUsersDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @DisplayName("유저 email 값으로 유저 정보를 찾아온다")
    @Test
    void findUserByEmail() {
        //given
        Users users = Users.of("testKim123", RoleEnum.USER);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(users));

        //when
        CustomUserDetail userDetails = (CustomUserDetail) customUserDetailsService.loadUserByUsername("testKim123");

        //then
        assertThat(userDetails.getEmail()).isEqualTo(users.getEmail());
        assertThat(userDetails.getAuthorities())
                .hasSize(1)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
        verify(userRepository, times(1)).findById(anyString());
    }

    @DisplayName("존재하지 않는 email 값으로 조회하면 예외가 발생한다")
    @Test
    void findUserByNotExistEmail() {
        //given
        when(userRepository.findById(anyString())).thenReturn(empty());

        //when & then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("testKim123"))
                .isInstanceOf(ApiException.class)
                .extracting("e")
                .extracting("errorCode", "errorMessage")
                .containsExactlyInAnyOrder("AUT_404_01", "존재하지 않는 사용자입니다.");
        verify(userRepository, times(1)).findById(anyString());
    }
}