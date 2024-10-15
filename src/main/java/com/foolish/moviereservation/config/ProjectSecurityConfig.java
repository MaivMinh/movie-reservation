package com.foolish.moviereservation.config;

import com.foolish.moviereservation.filters.CsrfTokenFilter;
import com.foolish.moviereservation.filters.JwtTokenGeneratorFilter;
import com.foolish.moviereservation.filters.JwtTokenValidatorFilter;
import com.foolish.moviereservation.filters.LoggingAuthoritiesFilter;
import com.foolish.moviereservation.handler.SpaCsrfTokenRequestAttributeHandler;
import com.foolish.moviereservation.security.CustomAccessDeniedHandler;
import com.foolish.moviereservation.security.CustomBasicAuthenticationEntryPoint;
import com.foolish.moviereservation.security.NonProdUsernamePwdAuthenticationProvider;
import com.foolish.moviereservation.security.UsernamePwdAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
            .ignoringRequestMatchers("/api/v1/auth/signup", "/api/v1/auth/login")
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestAttributeHandler())
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    );
    http.addFilterAfter(new CsrfTokenFilter(), BasicAuthenticationFilter.class);
    //http.addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class);  // Sau khi mà thực hiện việc xác thực thành công thì mới được phép tạo ra JWT Token tương ứng.
    http.addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class); // Nếu sử dụng JWT Authentication thì phải cho Filter này phía trước BasicAuthenticationFilter bởi vì Client sẽ không đính kèm credentials lần nào nữa nếu như Sign-In successfully.

    http.cors(corsConfig -> corsConfig
            .configurationSource(new CorsConfigurationSource() {
              @Override
              public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("*")); //Thay thế Origin này sau khi lên Production.
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(List.of("Authorization"));
                config.setMaxAge(3600L);
                return config;
              }
            }));
//    http.requiresChannel(config -> config.anyRequest().requiresSecure());
    http
            .authorizeHttpRequests(config -> config
                    .requestMatchers("/api/v1/auth/signup", "/api/v1/auth/login").permitAll()
                    .requestMatchers("api/v1/admin/**").hasAuthority("ADMIN")
                    .requestMatchers("api/v1/user/**").hasAuthority("USER")
                    .requestMatchers("/api/v1/public/**").permitAll()
                    .anyRequest().authenticated());
    http.httpBasic(config -> config.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
    http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));
    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  // Việc tạo ra Bean AuthenticationManager này chỉ áp dụng đối với các project yêu cầu việc login thông qua body của request.
  @Bean
  public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    UsernamePwdAuthenticationProvider authenticationProvider =
            new UsernamePwdAuthenticationProvider(userDetailsService, passwordEncoder);
    ProviderManager providerManager = new ProviderManager(authenticationProvider);
    providerManager.setEraseCredentialsAfterAuthentication(false);
    return providerManager;
  }

}
