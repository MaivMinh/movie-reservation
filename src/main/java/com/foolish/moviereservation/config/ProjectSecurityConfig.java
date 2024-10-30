package com.foolish.moviereservation.config;

import com.foolish.moviereservation.filters.CsrfTokenFilter;
import com.foolish.moviereservation.filters.JwtTokenValidatorFilter;
import com.foolish.moviereservation.handler.SpaCsrfTokenRequestAttributeHandler;
import com.foolish.moviereservation.security.CustomAccessDeniedHandler;
import com.foolish.moviereservation.security.CustomBasicAuthenticationEntryPoint;
import com.foolish.moviereservation.security.OwnUserDetailsService;
import com.foolish.moviereservation.security.UsernamePwdAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class ProjectSecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.csrf(config -> config
            .ignoringRequestMatchers(
                    "/api/v1/auth/**",
                    "/api/v1/s3/**",
                    "/swagger-ui/**",
                    "/v2/api-docs",
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/swagger-resources",
                    "/swagger-ui.html",
                    "/api/v1/admin/movies")
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestAttributeHandler())
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    );
    http.addFilterAfter(new CsrfTokenFilter(), BasicAuthenticationFilter.class);
    http.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class);

    http.cors(corsConfig -> corsConfig
            .configurationSource(request -> {
              CorsConfiguration config = new CorsConfiguration();
              config.setAllowedOrigins(Collections.singletonList("*")); //Thay thế Origin này sau khi lên Production.
              config.setAllowedMethods(Collections.singletonList("*"));
              config.setAllowCredentials(true);
              config.setAllowedHeaders(Collections.singletonList("*"));
              config.setExposedHeaders(List.of("Authorization"));
              config.setMaxAge(3600L);
              return config;
            }));
    http
            .authorizeHttpRequests(config -> config
                    .requestMatchers(
                            "/api/v1/auth/**",
                            "/api/v1/public/**", "/api/v1/s3/**").permitAll()
                    .requestMatchers("api/v1/admin/**").hasAuthority("ADMIN")
                    .requestMatchers("api/v1/users/**").hasAuthority("USER")
                    .anyRequest().authenticated());
    http.httpBasic(config -> config.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
    http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));
    return http.build();
  }

  /*
  * Bean phía dưới đây là Bean quan trọng trong việc hiển thị UI của OPEN API.
  * Nếu chúng ta sử dụng .permitAll() cho các endpoint phía dưới đây thì chúng ta không cần phải xác thực authen/author gì cả. Nhưng HttpServletRequest sẽ vẫn đi qua JwtTokenValidatorFilter. Nên do đó, sẽ bị throw ra Exception do không có Cookies theo yêu cầu.
  * Nhưng việc cấu hình .equals(...) cho từng endpoint phía dưới đây là việc thiếu linh hoạt và công kềnh. Nếu lỡ như sau này phát sinh các endpoint khác thì lại phải cập nhật thủ công lại bên trong JwtTokenValidatorFilter.
  * Nên chúng ta phải sử dụng @Bean WebSecurityCustomize này để cho phép các endpoint phía dưới của Swagger có thể đi qua Filter chain. Từ đó tránh được JwtTokenValidatorFilter.
  * */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/swagger-ui.html"
    );
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  // Việc tạo ra Bean AuthenticationManager này chỉ áp dụng đối với các project yêu cầu việc login thông qua body của request.
  @Bean
  public AuthenticationManager authenticationManager(OwnUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    UsernamePwdAuthenticationProvider authenticationProvider =
            new UsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
    ProviderManager providerManager = new ProviderManager(authenticationProvider);
    providerManager.setEraseCredentialsAfterAuthentication(false);
    return providerManager;
  }
}
