package com.frankit.shop.domain.product.api.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {
    @LocalServerPort
    public int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("상품을 조회할 수 있다")
    @Test
    void getProducts() {
        given()
                .log().all()
                .queryParams(Map.of(
                        "pageNumber", 0,
                        "pageSize", 10))
        .when()
                .get("/api/v1/product")
        .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    void createProducts() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }
}