package com.Tekarch.DataStoreService.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Allow CORS globally for all endpoints
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply CORS settings to all endpoints
                .allowedOrigins("http://localhost:4200")  // Allow your frontend's origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow all necessary methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // If you need cookies or other credentials
    }
}
