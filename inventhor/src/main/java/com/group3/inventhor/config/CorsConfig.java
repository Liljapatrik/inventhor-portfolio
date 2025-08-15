package com.group3.inventhor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author Tatiana Fløisbonn
 *
 * This configuration class sets up CORS (Cross-Origin Resource Sharing)
 * to allow your Spring Boot application to accept requests from a specific frontend domain.
 *
 * @Configuration indicates that this class contains Spring configuration.
 * @Bean indicates that the corsFilter method will return a bean
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
    // Create a new CORS configuration
        CorsConfiguration corsConfig = new CorsConfiguration();
    // Allow credentials (cookies, authentication headers)
    // This is required if you're sending authentication tokens
        corsConfig.setAllowCredentials(true);
    // Allow requests from your frontend domain(s)
    // Replace with your actual frontend URL
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
    // Allow specific HTTP methods that your API supports
    // OPTIONS is required for pre-flight requests
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE",
                "OPTIONS"));
    // Allow specific headers in the request
    // Authorization is needed for your JWT token
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    // Expose response headers to the frontend if needed
        corsConfig.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin"));
    // How long the browser should cache the CORS response (in seconds)
        corsConfig.setMaxAge(3600L);
    // Apply this configuration to all paths in your application
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
    // Create and return the CORS filter
        return new CorsFilter(source);
    }
}