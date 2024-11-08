package com.foolish.moviereservation.config;

import com.foolish.moviereservation.constants.ApplicationConstants;
import com.foolish.moviereservation.filters.CsrfTokenFilter;
import com.foolish.moviereservation.filters.JwtTokenValidatorFilter;
import com.foolish.moviereservation.handler.SpaCsrfTokenRequestAttributeHandler;
import com.foolish.moviereservation.security.CustomAccessDeniedHandler;
import com.foolish.moviereservation.security.CustomBasicAuthenticationEntryPoint;
import com.foolish.moviereservation.security.OwnUserDetailsService;
import com.foolish.moviereservation.security.UsernamePwdAuthenticationProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
@AllArgsConstructor
public class ProjectSecurityConfig {
  private final Environment env;

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
    http.addFilterAfter(new CsrfTokenFilter(), ExceptionTranslationFilter.class);
    http.addFilterBefore(new JwtTokenValidatorFilter(), ExceptionTranslationFilter.class);
    http
            .authorizeHttpRequests(config -> config
                    .requestMatchers(
                            "/api/v1/auth/**",
                            "/api/v1/public/**", "/api/v1/s3/**").permitAll()
                    .requestMatchers("api/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated());
    http.httpBasic(config -> config.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
    http.oauth2Login(config -> config.defaultSuccessUrl(ApplicationConstants.DEFAULT_ORIGIN_URL));
    http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers(
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-resources",
            "/swagger-ui.html",
            "/api/v1/auth/**",
            "/favicon.ico", "/"
    );
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(ApplicationConstants.DEFAULT_ORIGIN_URL).allowCredentials(true).exposedHeaders("*").allowedMethods("*");
      }
    };
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

  private ClientRegistration googleRegistration() {
    String googleClientId = env.getProperty("google.clientId");
    String googleClientSecret = env.getProperty("google.clientSecret");
    return CommonOAuth2Provider.GOOGLE.getBuilder("google").clientId(googleClientId).clientSecret(googleClientSecret).build();
  }

  @Bean
  ClientRegistrationRepository clientRegistrationRepository() {
    // Tạo ra các registration object. Mỗi object này đại diện cho 1 Auth Server mà chúng ta đã đăng kí.
    ClientRegistration google = googleRegistration();
    return new InMemoryClientRegistrationRepository(google);
  }
}
