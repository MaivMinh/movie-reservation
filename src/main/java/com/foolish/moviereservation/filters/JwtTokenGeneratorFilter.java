package com.foolish.moviereservation.filters;

import com.foolish.moviereservation.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

// Filter này thực hiện việc tạo ra một JWT Token sau khi BasicAuthenticationFilter thực hiện xong.
// Việc nó có tạo ra Token hay không còn phụ thuộc vào Authentication object có hay không. Nếu xác thực User thành công thì có và ngược lại.
public class JwtTokenGeneratorFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      // Nếu quá trình xác thực bởi BasicAuthenticationFilter thành công thì Authentication object sẽ được lưu bên trong SecurityContextHolder và ngược lại.
      Environment env = getEnvironment();
      String secret = env.getProperty("SECRET_KEY", ApplicationConstants.SECRET_KET);

      SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
      String token = Jwts
              .builder()
              .setIssuer("Movie Reservation System")
              .setSubject("JWT TOKEN")
              .claim("username", authentication.getName())
              .claim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
              .setIssuedAt(new Date())
              .setExpiration(new Date((new Date().getTime()) + 86400000))// 1 day.
              .signWith(secretKey, SignatureAlgorithm.HS256)
              .compact();
      response.setHeader(ApplicationConstants.HEADER_FOR_JWT, token);  // Authorization ...
    }
    filterChain.doFilter(request, response);
  }
}
