package com.foolish.moviereservation.filters;

import com.foolish.moviereservation.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// Filter này sẽ được dùng để đánh chặn trước các Private API khi vào trong Filters chain.
// Mục đích của Filter này dùng để kiểm tra xem Client có cung cấp Token phù hợp hay không:
// 1. Nếu Token phù hợp thì Forward tiếp request cho các Filter phía sau.
// 2. Nếu không phù hợp:
// 2.1 Nếu Token không chính xác thì Throw ra lỗi BadCredentialsException.
// 2.2 Nếu Token hết hạn thì throw ra ExpiredJwtException. Và sau đó gửi Response về thông báo cho Client cần Login lại.

public class JwtTokenValidatorFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    StringBuilder accessToken = new StringBuilder();
    // Extract Authorization header.
    String value = request.getHeader("Authorization");
    accessToken.append(value.substring(7));
    if (!accessToken.isEmpty()) {
      try {
        Environment env = getEnvironment();
        String secret = env.getProperty("SECRET_KEY");
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken.toString()).getBody();
        String username = claims.get("username").toString();
        String roles = String.valueOf(claims.get("roles"));
        System.out.println(roles);
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(roles));
        //Thêm authenticated object vào SecurityContextHolder.
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (ExpiredJwtException e) {
        throw new BadCredentialsException("Expired JWT token");
      } catch (SignatureException e) {
        throw new BadCredentialsException("Invalid signature!", e);
      } catch (RuntimeException e) {
        throw new RuntimeException("Validate JWT token failed");
      }
    } else throw new BadCredentialsException("Token not found!");
    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return (path.startsWith("/api/v1/auth") || path.startsWith("/api/v1/movies") || path.startsWith("/api/v1/cinemas"));
  }
}
