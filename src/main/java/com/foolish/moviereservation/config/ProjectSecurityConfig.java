package com.foolish.moviereservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(config -> config.ignoringRequestMatchers("/api/**"));
    http
            .authorizeHttpRequests(config -> config
                    .requestMatchers("/api/v1/auth/sign-up").permitAll()
                    .requestMatchers("/api/v1/public/**").permitAll()
                    .requestMatchers("/api/v1/**").authenticated()
                    .requestMatchers("api/v1/admin/**").hasRole("ADMIN"));
    http.httpBasic(Customizer.withDefaults());
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
