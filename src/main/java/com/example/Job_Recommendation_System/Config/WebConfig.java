package com.example.Job_Recommendation_System.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://127.0.0.1:5173, https://careervistaa.vercel.app ") // Your front-end origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these methods
                        .allowedHeaders("*")
                        .allowCredentials(true) // Allow credentials if needed
                        .maxAge(3600); // Cache the preflight response for 1 hour
            }
        };
    }
}