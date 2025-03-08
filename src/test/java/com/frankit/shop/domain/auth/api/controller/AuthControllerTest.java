package com.frankit.shop.domain.auth.api.controller;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @LocalServerPort
    public int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("로그인을 하면 토큰을 발급받는다")
    @Test
    void validUserCanSignIn() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"email\":\"usertest123@naver.com\"" +
                        ", \"password\":\"password123\"}")
        .when()
                .post("/api/v1/auth/sign-in")
        .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true)
                ,"data", notNullValue(String.class));
    }

    @DisplayName("잘못된 계정을 로그인 하면 예외가 발생한다")
    @Test
    void inValidUserCanNotSignIn() {
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"email\":\"nouserEmail123@naver.com\"" +
                        ", \"password\":\"password123\"}")
        .when()
                .post("/api/v1/auth/sign-in")
        .then()
                .log().all()
                .statusCode(500)
                .body("success", equalTo(false)
                        ,"data", Matchers.nullValue());
    }

    @DisplayName("발급된 RefreshToken으로 토큰 재발급 요청할 경우 토큰을 반환한다")
    @Test
    void validRefreshTokenCanReissueToken() {
        String refreshToken =
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"email\":\"usertest123@naver.com\"" +
                        ", \"password\":\"password123\"}")
        .when()
                .post("/api/v1/auth/sign-in")
                .body().jsonPath().getString("data.refreshToken")
                ;

        // 재발급
        given()
                .log().all()
                .header("Authorization", refreshToken)
        .when()
                .post("/api/v1/auth/reissue-token")
        .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true)
                        ,"data", notNullValue(String.class));
    }

    @DisplayName("잘못된 RefreshToken으로 토큰 재발급 요청할 경우 예외가 발생한다")
    @Test
    void invalidRefreshTokenCanReissueToken() {
        String refreshToken =
                given()
                        .log().all()
                        .contentType("application/json")
                        .body("{\"email\":\"usertest123@naver.com\"" +
                                ", \"password\":\"password123\"}")
                .when()
                        .post("/api/v1/auth/sign-in")
                        .body().jsonPath().getString("data.refreshToken")
                ;

        // 재발급
        given()
                .log().all()
                .header("Authorization", refreshToken + "fail")
        .when()
                .post("/api/v1/auth/reissue-token")
        .then()
                .log().all()
                .statusCode(401)
                .body("success", equalTo(false)
                        ,"data", Matchers.nullValue());
    }
}