package com.frankit.shop.domain.product.api.controller;

import com.frankit.shop.domain.product.dto.ProductRequest;
import com.frankit.shop.domain.product.entity.Product;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    private static String accessToken;

    @LocalServerPort
    public int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        accessToken =
        given()
                .contentType("application/json")
                .body("{\"email\":\"usertest123@naver.com\"" +
                        ", \"password\":\"password123\"}")
        .when()
                .post("/api/v1/auth/sign-in")
                .body().jsonPath().getString("data.accessToken");
    }

    @DisplayName("토큰을 발급 받은 유저는 상품을 조회할 수 있다")
    @Test
    void validTokenGetProducts() {
        given()
                .log().all()
                .header("Authorization", accessToken)
                .queryParams(Map.of(
                        "page", 0,
                        "size", 10))
        .when()
                .get("/api/v1/product")
        .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true)
                ,"data.content",  hasSize(10)
                , "data.pageable.pageNumber", equalTo(0)
                , "data.pageable.pageSize", equalTo(10)
        );
    }

    @DisplayName("토큰이 없으면 상품을 조회할 수 없다")
    @Test
    void invalidTokenGetProducts() {
        given()
                .log().all()
                .header("Authorization", "")
                .queryParams(Map.of(
                        "page", 0,
                        "size", 10))
        .when()
                .get("/api/v1/product")
        .then()
                .log().all()
                .statusCode(401)
                .body("success", equalTo(false)
                , "data", nullValue()
        );
    }

    @DisplayName("ADMIN 권한을 가진 사용자는 상품을 등록할 수 있다")
    @Test
    void adminCanCreateProducts() {
        Product product = Product.of("모니터", "모니터입니다", 500_000, 5000);

        accessToken =
                given()
                        .contentType("application/json")
                        .body("{\"email\":\"admintest123@naver.com\"" +
                                ", \"password\":\"password123\"}")
                .when()
                        .post("/api/v1/auth/sign-in")
                        .body().jsonPath().getString("data.accessToken");

        given()
                .log().all()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(product)
        .when()
                .post("/api/v1/product")
        .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true)
        );
    }

    @DisplayName("ADMIN 권한 외 사용자는 상품을 등록할 수 없다")
    @Test
    void notAdminCanNotCreateProducts() {
        Product product = Product.of("모니터", "모니터입니다", 500_000, 5000);

        given()
                .log().all()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(product)
        .when()
                .post("/api/v1/product")
        .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false)
                , "data", nullValue()
        );
    }

    @DisplayName("ADMIN 권한을 가진 사용자는 상품을 수정할 수 있다")
    @Test
    void adminCanUpdateProducts() {
        ProductRequest productRequest = ProductRequest.of("모니터_ver.0.2", "모니터수정본입니다", 500_000, 5000);

        accessToken =
                given()
                        .contentType("application/json")
                        .body("{\"email\":\"admintest123@naver.com\"" +
                                ", \"password\":\"password123\"}")
                        .when()
                        .post("/api/v1/auth/sign-in")
                        .body().jsonPath().getString("data.accessToken");

        given()
                .log().all()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(productRequest)
        .when()
                .put("/api/v1/product/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true)
        );
    }

    @DisplayName("ADMIN 권한 외 사용자는 상품을 수정할 수 없다")
    @Test
    void notAdminCanNotUpdateProducts() {
        ProductRequest productRequest = ProductRequest.of("모니터_ver.0.2", "모니터수정본입니다", 500_000, 5000);

        given()
                .log().all()
                .header("Authorization", accessToken)
                .contentType("application/json")
                .body(productRequest)
        .when()
                .put("/api/v1/product/1")
        .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false)
        );
    }

    @DisplayName("ADMIN 권한을 가진 사용자는 상품을 삭제할 수 있다")
    @Test
    void adminCanDeleteProducts() {
        accessToken =
                given()
                        .contentType("application/json")
                        .body("{\"email\":\"admintest123@naver.com\"" +
                                ", \"password\":\"password123\"}")
                        .when()
                        .post("/api/v1/auth/sign-in")
                        .body().jsonPath().getString("data.accessToken");

        given()
                .log().all()
                .header("Authorization", accessToken)
                .contentType("application/json")
        .when()
                .delete("/api/v1/product/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("success", equalTo(true)
        );
    }

    @DisplayName("ADMIN 권한 외 사용자는 상품을 삭제할 수 없다")
    @Test
    void notAdminCanNotDeleteProducts() {
        accessToken =
                given()
                        .contentType("application/json")
                        .body("{\"email\":\"usertest123@naver.com\"" +
                                ", \"password\":\"password123\"}")
                        .when()
                        .post("/api/v1/auth/sign-in")
                        .body().jsonPath().getString("data.accessToken");

        given()
                .log().all()
                .header("Authorization", accessToken)
                .contentType("application/json")
        .when()
                .delete("/api/v1/product/1")
        .then()
                .log().all()
                .statusCode(403)
                .body("success", equalTo(false)
        );
    }

}