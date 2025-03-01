package com.frankit.shop.global.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi shopOpenApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .addOpenApiCustomizer(openApi -> openApi.setInfo(
                        new Info()
                                .title("SHOP API")
                                .version("1.0.0")
                                .description("SHOP API v1")
                ))
                .build();
    }
}
