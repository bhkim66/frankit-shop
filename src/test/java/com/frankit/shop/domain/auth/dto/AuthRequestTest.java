package com.frankit.shop.domain.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validatorFromFactory;

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validatorFromFactory = validatorFactory.getValidator();
    }

    @DisplayName("형식에 맞는 데이터는 validation을 통과한다")
    @Test
    void validateRequest() {
        //given
        String email = "testerKim123@naver.com";
        String password = "password123";
        AuthRequest.SignIn signIn = AuthRequest.SignIn.of(email, password);
        Set<ConstraintViolation<AuthRequest.SignIn>> validate = validatorFromFactory.validate(signIn);

        //when & then
        assertThat(validate).isEmpty();
    }

    @DisplayName("이메일 형식에 맞지 않는 request는 예외가 발생한다")
    @Test
    void emailInvalid() {
        //given
        String email = "invalidEmailForm";
        String password = "password123";
        AuthRequest.SignIn signIn = AuthRequest.SignIn.of(email, password);
        Set<ConstraintViolation<AuthRequest.SignIn>> validate = validatorFromFactory.validate(signIn);

        //when & then
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsExactly("이메일 양식을 지켜주세요");
    }

    @DisplayName("이메일이 빈값이라면 request는 예외가 발생한다")
    @Test
    void emptyEmailInvalid() {
        //given
        String email = "";
        String password = "password123";
        AuthRequest.SignIn signIn = AuthRequest.SignIn.of(email, password);
        Set<ConstraintViolation<AuthRequest.SignIn>> validate = validatorFromFactory.validate(signIn);

        //when & then
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsExactly("이메일을 입력해주세요");
    }

    @DisplayName("패스워드가 빈값이라면 request는 예외가 발생한다")
    @Test
    void emptyPasswordInvalid() {
        //given
        String email = "testkim@naver.com";
        String password = "";
        AuthRequest.SignIn signIn = AuthRequest.SignIn.of(email, password);
        Set<ConstraintViolation<AuthRequest.SignIn>> validate = validatorFromFactory.validate(signIn);

        //when & then
        assertThat(validate).extracting(ConstraintViolation::getMessage).containsExactly("비밀번호를 입력해주세요");
    }


}