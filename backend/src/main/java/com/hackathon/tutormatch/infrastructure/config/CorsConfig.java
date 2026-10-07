package com.hackathon.tutormatch.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final String ORIGEN_DESARROLLO = "http://localhost:5173";

    private final String frontendUrl;

    public CorsConfig(@Value("${FRONTEND_URL:}") String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origenes = new ArrayList<>(List.of(ORIGEN_DESARROLLO));
        if (!frontendUrl.isBlank()) {
            // El navegador envia el Origin sin "/" final
            origenes.add(frontendUrl.trim().replaceAll("/+$", ""));
        }
        registry.addMapping("/api/**")
                .allowedOrigins(origenes.toArray(String[]::new))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
