package com.example.carsharingservice.config;

import com.example.carsharingservice.model.User;
import com.example.carsharingservice.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth.requestMatchers(
                                 HttpMethod.POST, "/register", "/login",
                                "/payments/webhook").permitAll()
                                .requestMatchers("/test", "/swagger-ui/**", "/swagger-ui.html",
                                        "/v3/api-docs/**").permitAll()
                                .requestMatchers("/users/me")
                                .hasAnyRole(User.Role.MANAGER.name(), User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.PUT, "/users/{id}/role")
                                .hasRole(User.Role.MANAGER.name())
                                .requestMatchers(HttpMethod.POST, "/cars")
                                .hasRole(User.Role.MANAGER.name())
                                .requestMatchers(HttpMethod.GET, "/cars")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/cars/{id}")
                                .hasAnyRole(User.Role.MANAGER.name(), User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.PUT, "/cars/{id}")
                                .hasRole(User.Role.MANAGER.name())
                                .requestMatchers(HttpMethod.DELETE, "/cars/{id}")
                                .hasRole(User.Role.MANAGER.name())
                                .requestMatchers(HttpMethod.POST, "/rentals",
                                        "/rentals/{id}/return")
                                .hasAnyRole(User.Role.MANAGER.name(), User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.GET, "/rentals/{id}")
                                .hasAnyRole(User.Role.MANAGER.name(), User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.GET, "/rentals")
                                .hasRole(User.Role.MANAGER.name())
                                .requestMatchers(HttpMethod.POST, "/rentals")
                                .hasRole(User.Role.MANAGER.name())
                                .requestMatchers(HttpMethod.POST, "/payments")
                                .hasRole(User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.GET, "/payments")
                                .hasAnyRole(User.Role.MANAGER.name(),
                                        User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.GET, "/payments/success",
                                        "/payments/cancel")
                                .hasRole(User.Role.CUSTOMER.name())
                                .requestMatchers(HttpMethod.POST, "/payments/{paymentId}/renew")
                                .hasAnyRole(User.Role.MANAGER.name(), User.Role.CUSTOMER.name())
                                .anyRequest()
                                .authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }
}
