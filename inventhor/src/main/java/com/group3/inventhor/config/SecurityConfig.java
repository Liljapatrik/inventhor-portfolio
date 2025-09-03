package com.group3.inventhor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Tatiana Fløisbonn
 *
 * This class configures Spring Security for the application.
 * It sets up security filters, session management, and JWT authentication.
 * It also includes a custom converter to handle Keycloak roles and convert them into Spring Security's GrantedAuthority format.
 * This is essential for integrating Keycloak with Spring Security, allowing the application to recognize and authorize users based on their roles defined in Keycloak.
 *
 * This configuration allows all requests to the Swagger UI and API documentation endpoints,
 * while requiring authentication for all other requests.
 * It also disables CSRF protection and sets the session management policy to stateless,
 * which is typical for REST APIs that use token-based authentication like JWT.
 */
@Configuration // Tells Spring this is a configuration class
@EnableWebSecurity // Enables Spring Security web security support
@EnableMethodSecurity // Enables @PreAuthorize annotations
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );
        return http.build();
    }

    /**
     * @Bean indicate that a method produces a bean to be managed by the Spring container.
     * It means that Spring will execute that method and register its return value as a bean in the application context,
     * making it available for dependency injection elsewhere in your application.
     *
     * @return JwtAuthenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;

    }

    /**
     * This class converts Keycloak roles found in the JWT token
     * into Spring Security's GrantedAuthority format.
     * <p>
     * Keycloak stores roles differently than Spring Security expects them,
     * so we need this converter to bridge the gap.
     */
    static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {

            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");

            if (resourceAccess != null && resourceAccess.containsKey("inventhor-app")) {

                // Get the roles from the "inventhor-app" resource access
                @SuppressWarnings("unchecked")
                Map<String, Object> inventhorApp = (Map<String, Object>) resourceAccess.get("inventhor-app");

                // Extract roles from the "inventhor-app" resource access
                if (inventhorApp.containsKey("roles")) {
                    @SuppressWarnings("unchecked")
                    Collection<String> roles = (Collection<String>) inventhorApp.get("roles");
                    roles.forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                    });
                }
            }

            return authorities;
        }

    }

}