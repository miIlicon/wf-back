package com.festival.common.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v2.0.0")
                .title("With Festival API")
                .description("With Festival v2 api spec 입니다.");

        return new OpenAPI()
                .info(info);
    }
}
