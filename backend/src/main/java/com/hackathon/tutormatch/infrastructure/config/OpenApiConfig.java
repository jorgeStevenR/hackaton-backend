package com.hackathon.tutormatch.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI tutorMatchOpenApi() {
        return new OpenAPI().info(new Info()
                .title("TutorMatch API")
                .description("Matching de tutorias entre pares: recomienda el tutor ideal para una solicitud "
                        + "segun horarios, nivel, calificacion y modalidad, con un score de afinidad explicado.")
                .version("1.0.0"));
    }
}
