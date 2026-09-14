package com.glowup.backend;


import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        String allowedOrigins = System.getenv().getOrDefault("CORS_ORIGINS", "http://127.0.0.1:5500,http://localhost:5500");
        String[] origins = allowedOrigins.split(",");
        for (int index = 0; index < origins.length; index++) {
            origins[index] = origins[index].trim();
        }
        registry.addMapping("/api/**")
            .allowedOrigins(origins)
            .allowedMethods("GET", "POST", "OPTIONS")
            .allowedHeaders("*");
    }
}
