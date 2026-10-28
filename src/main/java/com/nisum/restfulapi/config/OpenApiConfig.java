package com.nisum.restfulapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI registerOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API de registro de usuarios")
                .version("1.0.0")
                .description("API RESTful para crear usuarios. Todos los endpoints aceptan y retornan "
                        + "solo JSON, y los errores siempre tienen el formato {\"mensaje\": \"...\"}."));
    }

}
