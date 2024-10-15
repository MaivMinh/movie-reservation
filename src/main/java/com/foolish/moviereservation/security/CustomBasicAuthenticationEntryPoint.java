package com.foolish.moviereservation.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    String timeStamp = LocalDateTime.now().toString();
    String message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
    String path = request.getServletPath();
    System.out.println(path);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setHeader("application-error-reason", "Authentication failed");
    String jsonResponse = String.format("{" +
            "\"timestamp\":" + "\"%s\"\n" +
            "\t\"status\":" + "\"%s\"\n" +
            "\t\"error\":" + "\"%s\"\n" +
            "\t\"message\":" + "\"%s\"\n" +
            "\t\"path\":" + "\"%s\"\n" +
            "}", timeStamp, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message, path);
    response.getWriter().write(jsonResponse);
  }
}


