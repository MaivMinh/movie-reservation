package com.foolish.moviereservation.config;

import com.foolish.moviereservation.security.CustomAccessDeniedHandler;
import com.foolish.moviereservation.security.CustomBasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@Profile("prod")
public class ProjectSecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(corsConfig -> corsConfig
            .configurationSource(new CorsConfigurationSource() {
              @Override
              public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setMaxAge(3600L);
                config.setAllowedOrigins(Collections.singletonList("*")); //Thay thế Origin này sau khi lên Production.
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                return config;
              }
            }));
    http.requiresChannel(config -> config.anyRequest().requiresSecure());
    http.csrf(config -> config.ignoringRequestMatchers("/api/**"));
    http
            .authorizeHttpRequests(config -> config
                    .requestMatchers("/api/v1/auth/sign-up").permitAll()
                    .requestMatchers("/api/v1/public/**").permitAll()
                    .requestMatchers("/api/v1/**").authenticated()
                    .requestMatchers("api/v1/admin/**").hasRole("ADMIN"));
    http.httpBasic(config -> config.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
    http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
