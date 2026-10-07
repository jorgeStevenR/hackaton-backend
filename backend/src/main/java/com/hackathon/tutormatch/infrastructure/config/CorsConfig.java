package com.hackathon.tutormatch.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /** Frontend en desarrollo (Vite) y frontend desplegado en Vercel. */
    private static final List<String> ORIGENES_FIJOS = List.of(
            "http://localhost:5173",
            "https://hackaton-beta-seis.vercel.app");

    private final String frontendUrl;

    public CorsConfig(@Value("${FRONTEND_URL:}") String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origenes = new ArrayList<>(ORIGENES_FIJOS);
        // FRONTEND_URL admite varias URLs separadas por comas; el navegador envia el Origin sin "/" final
        Arrays.stream(frontendUrl.split(","))
                .map(url -> url.trim().replaceAll("/+$", ""))
                .filter(url -> !url.isEmpty() && !origenes.contains(url))
                .forEach(origenes::add);

        registry.addMapping("/**")
                .allowedOrigins(origenes.toArray(String[]::new))
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Location")
                .maxAge(3600);
    }
}
